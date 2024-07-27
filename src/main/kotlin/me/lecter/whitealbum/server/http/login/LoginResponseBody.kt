package me.lecter.whitealbum.server.http.login

import me.lecter.whitealbum.client.body.LoginData

class LoginResponseBody(
    val message: String,
    val data: LoginData?,
)
