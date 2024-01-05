const TokenType = {
    ACCESS: "accessToken",
    REFRESH: "refreshToken"
}

const Site = {
    INDEX: "index.html",
    SIGNIN: "signin.html"
}

function redirect(site) {
    $(location).prop('href', `http://127.0.0.1:8080/${site}`);
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

function asyncAjax({ method, url, data }) {
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
            }
        });
    });
}

async function requestAccessToken() {
    try {
        const refreshToken = getToken(TokenType.REFRESH);
        if (!refreshToken) {
            redirect(Site.SIGNIN)
            return;
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
        const refreshToken = getToken(TokenType.REFRESH);
        if (!refreshToken) {
            redirect(Site.SIGNIN)
        }
        requestAccessToken();
        setInterval(() => requestAccessToken(), 5000);
    }
}

window.onload = updateAccessToken();