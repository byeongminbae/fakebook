const TokenType = {
    ACCESS: "accessToken",
    REFRESH: "refreshToken"
}

const Site = {
    INDEX: "index.html",
    SIGNIN: "signin.html"
}

function redirect(site) {
    $(location).prop('href', `http://localhost:8080/${site}`);
}

function decodeToken(token) {
    const decodedToken = {};
    try {
        const tokenParts = token.split('.');
        decodedToken.header = JSON.parse(atob(tokenParts[0]));
        decodedToken.payload = JSON.parse(atob(tokenParts[1]));
        decodedToken.signature = tokenParts[2];
    } catch (e) {
        console.log(e);
    }
    return decodedToken;
}

function getCookies() {
    return document.cookie.split(';').map(cookie => cookie.trim());
}

function setCookie(cookieName, cookieValue, expiredAt) {
    document.cookie = cookieName + "=" + cookieValue + ";" + `expires=${new Date(expiredAt).toString()};` + ";path=/";
}


function getToken(tokenType) {
    const cookie = getCookies().find(cookie => cookie.includes(`${tokenType}=`));
    if (cookie) {
        return cookie.split("=")[1]
    }
}

function asyncAjax({ method, url, data, accessToken }) {
    return new Promise(function (resolve, reject) {
        $.ajax({
            method: method,
            url: url,
            contentType: "application/json",
            dataType: "json",
            data: data,
            beforeSend: function () {
            },
            success: function (data) {
                resolve(data)
            },
            error: function (err) {
                reject(err)
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
            },
        });
    });
}

async function requestAccessToken() {
    try {
        const refreshToken = getToken(TokenType.REFRESH);
        if (!refreshToken) {
            redirect(Site.SIGNIN)
            return
        }
        const response = await asyncAjax({
            method: "POST",
            url: '/auth/token/renew',
            data: JSON.stringify({ refreshToken: refreshToken }),
        })
        if (response.success) {
            setCookie("accessToken", response.data.token, response.data.expiredAt);
            return response;
        }
    } catch (e) {
        console.log(e);
    }
}

async function requestSignIn(id, password) {
    const response = await asyncAjax({
        method: "POST",
        url: "/auth/token/sign-in",
        data: JSON.stringify({ signId: id, signPassword: password })
    })

    if (response.success) {
        setCookie("accessToken", response.data.accessTokenResponseDto.token, response.data.accessTokenResponseDto.expiredAt);
        setCookie("refreshToken", response.data.refreshTokenResponseDto.token, response.data.refreshTokenResponseDto.expiredAt)
        redirect(Site.INDEX)
        return response;
    }
    alert("ID or Password incorrect");
}

async function updateAccessToken() {
    if (window.location.pathname != `/${Site.SIGNIN}`) {
        requestAccessToken();
        setInterval(() => requestAccessToken(), 5000);
    }
}

async function loadChannels() {
    const response = await asyncAjax({
        method: "GET",
        url: "/channels",
    })

    response.data.forEach(item => {
        const html = `
            <div class="col-sm-6 col-lg-4 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">${item.title}</h5>
                        <p class="card-text">${item.description}</p>
                        <p class="card-text"><small>memberCount: ${item.memberCount}</small></p>
                        <p class="card-text"><small>chatCount: ${item.chatCount}</small></p>
                        <p class="card-text"><small>creator: ${item.creatorSignId}</small></p>
                        <p class="card-text"><small class="text-muted">createdAt: ${item.createdAt}</small></p>
                        <button class="btn btn-primary enter-btn" data-channel-id="${item.id}">Enter</button>
                    </div>
                </div>
            </div>
        `;
        $("#channels-list").append(html)
    });
    $(".enter-btn").click(function () {
        const channelId = $(this).data("channel-id");
        joinChannel(channelId);
    });
}

async function joinChannel(channelId) {
    const accessToken = getToken(TokenType.ACCESS);
    const decodedToken = decodeToken(accessToken);
    await asyncAjax({
        method: "POST ",
        url: `/channels/${channelId}/members/${decodedToken.payload.memberId}`,
        accessToken: accessToken
    })
}
let socket = null; // 전역 변수로 선언하여 다른 함수에서도 접근 가능하도록 설정

async function loadMyChannels() {
    const accessToken = getToken(TokenType.ACCESS);
    const decodedToken = decodeToken(accessToken);

    const response = await asyncAjax({
        method: "GET",
        url: `/members/${decodedToken.payload.memberId}/channels`,
        accessToken: accessToken
    })

    response.data.forEach(item => {
        const html = `
            <div class="channel card col-sm-6 col-lg-4 mb-4" data-channel-id="${item.id}">
                    <div class="card-body">
                        <h5 class="card-title">${item.title}</h5>
                        <p class="card-text">${item.description}</p>
                        <p class="card-text"><small>memberCount: ${item.memberCount}</small></p>
                        <p class="card-text"><small>chatCount: ${item.chatCount}</small></p>
                        <p class="card-text"><small>creator: ${item.creatorSignId}</small></p>
                        <p class="card-text"><small class="text-muted">createdAt: ${item.createdAt}</small></p>
                </div>
            </div>
        `;
        $("#channels-list").append(html)
    });
    $(".channel").click(function () {
        const channelId = $(this).data("channel-id");

        if (socket !== null && socket.readyState === WebSocket.OPEN) {
            socket.close();
        }

        initializeWebSocket(channelId);
    });
}

function initializeWebSocket(channelId) {
    const accessToken = getToken(TokenType.ACCESS);

    socket = new WebSocket(`ws://127.0.0.1:8080/chat?channelId=${channelId}&accessToken=${accessToken}`);

    socket.onopen = function (event) {
        console.log('WebSocket connected');
        setupMessageHandler(socket);
        setupSendHandler(socket);
    };
}

function setupMessageHandler(socket) {
    socket.onmessage = function (event) {
        const message = event.data;
        $('#chat-box').append(`<p>${message}</p>`);
    };
}

function setupSendHandler(socket) {
    $('#send-button').off('click').on('click', function () {
        const message = $('#chat-input').val();
        if (message.trim() !== '') {
            socket.send(message);
            $('#chat-box').append(`<p>${message}</p>`);
            $('#chat-input').val('');
        }
    });

    $('#chat-input').off('keypress').on('keypress', function (e) {
        if (e.which === 13) {
            const message = $('#chat-input').val();
            if (message.trim() !== '') {
                socket.send(message);
                $('#card-body').append(`<p>${message}</p>`);
                $('#chat-input').val('');
            }
        }
    });
}

window.onload = updateAccessToken();