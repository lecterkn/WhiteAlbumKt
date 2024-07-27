package me.lecter.whitealbum.server.service.login

import me.lecter.whitealbum.WhiteAlbum
import me.lecter.whitealbum.client.RiotClient
import me.lecter.whitealbum.client.ValorantAPI
import me.lecter.whitealbum.client.exceptions.RiotException
import me.lecter.whitealbum.client.valorant.StoreFront
import org.springframework.stereotype.Service

@Service
class LoginServiceImpl : LoginService {
    override fun login(
        username: String,
        password: String,
        remember: Boolean,
    ): StoreFront? {
        val client = RiotClient(username, password)

        // ログイン処理
        try {
            client.login()
        } catch (e: RiotException) {
            e.printStackTrace()
            return null
        }

        if (!client.logged || client.access_token.isNullOrEmpty() || client.entitlements_token.isNullOrEmpty()) {
            println("loggin error")
            return null
        }

        if (remember) {
            WhiteAlbum.accountManager.save(username, password)
        }

        val api = ValorantAPI(client.access_token!!, client.entitlements_token!!)
        return api.getStoreFront()
    }
}
