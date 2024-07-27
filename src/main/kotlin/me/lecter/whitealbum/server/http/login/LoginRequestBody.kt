package me.lecter.whitealbum.server.http.login

class LoginRequestBody(
    val username: String,
    val password: String,
    val remember: Boolean,
)
