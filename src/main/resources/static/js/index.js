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
    const date = new Date(expiredAt).toLocaleString();
    document.cookie = cookieName + "=" + cookieValue + ";" + `expiredAt=${date};` + ";path=/";
}


function getToken(tokenType) {
    return getCookies().find(cookie => cookie.startsWith(tokenType + '='));
}

function checkTokens() {
    if (window.location.pathname != `/${Site.SIGNIN}`) {
        setInterval(() => {
            const accessToken = getToken(TokenType.ACCESS);
            const refreshToken = getToken(TokenType.REFRESH);

            if (!accessToken) {
                if (!refreshToken) {
                    redirect(Site.SIGNIN)
                }
                $.ajax({
                    method: 'POST',
                    url: '/auth/token/renew',
                    contentType: "application/json",
                    dataType: "json",
                    data: JSON.stringify({ refreshToken: refreshToken }),
                    success: function (response) {
                        if (response.success) {
                            setCookie("accessToken", response.data.accessTokenResponseDto.token, response.data.accessTokenResponseDto.expiredAt);
                            console.log(response)
                            return;
                        }
                        redirect(Site.SIGNIN)
                    }
                });
            }

        }, 5000);
    }
}

function requestSignIn(id, password) {
    $.ajax({
        type: "POST",
        url: "http://127.0.0.1:8080/auth/token/sign-in",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({ signId: id, signPassword: password }),
        success: function (response) {
            if (response.success) {
                setCookie("accessToken", response.data.accessTokenResponseDto.token, response.data.accessTokenResponseDto.expiredAt);
                setCookie("refreshToken", response.data.refreshTokenResponseDto.token, response.data.refreshTokenResponseDto.expiredAt)
                redirect(Site.INDEX)
                return;
            }
            alert("ID or Password incorrect");
        },
    });
}


