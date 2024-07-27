package me.lecter.whitealbum.server.http.account

import me.lecter.whitealbum.client.body.LoginData

class AccountResponseBody(
    var message: String,
    var data: LoginData?,
)
