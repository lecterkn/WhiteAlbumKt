package me.lecter.whitealbum.server.service.login

import me.lecter.whitealbum.client.valorant.StoreFront
import org.springframework.stereotype.Service

@Service
interface LoginService {
    fun login(
        username: String,
        password: String,
        remember: Boolean,
    ): StoreFront?
}
