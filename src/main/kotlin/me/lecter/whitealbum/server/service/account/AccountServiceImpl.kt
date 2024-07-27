package me.lecter.whitealbum.server.service.account

import me.lecter.whitealbum.WhiteAlbum
import me.lecter.whitealbum.client.RiotClient
import me.lecter.whitealbum.client.ValorantAPI
import me.lecter.whitealbum.client.exceptions.RiotException
import me.lecter.whitealbum.client.valorant.StoreFront
import me.lecter.whitealbum.server.http.account.AccountResponseBody
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AccountServiceImpl: AccountService {

    override fun getUsers(): List<String> {
        return WhiteAlbum.accountManager.accounts.map { it.username }
    }

    override fun login(username: String): StoreFront? {
        for (account in WhiteAlbum.accountManager.accounts) {
            if (account.username != username) {
                continue
            }
            val client = RiotClient(account.username, account.password)
            try {
                client.login()
            } catch (e: RiotException) {
                e.printStackTrace()
                break
            }
            if (!client.logged || client.access_token.isNullOrEmpty() || client.entitlements_token.isNullOrEmpty()) {
                break
            }

            val api = ValorantAPI(client.access_token!!, client.entitlements_token!!)
            return api.getStoreFront()
        }
        return null
    }
}