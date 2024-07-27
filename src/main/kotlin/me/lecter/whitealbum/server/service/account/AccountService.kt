package me.lecter.whitealbum.server.service.account

import me.lecter.whitealbum.client.valorant.StoreFront
import org.springframework.stereotype.Service

@Service
interface AccountService {

    fun getUsers(): List<String>

    fun login(username: String): StoreFront?
}