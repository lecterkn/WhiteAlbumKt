package me.lecter.whitealbum.server.http.account

import me.lecter.whitealbum.WhiteAlbum
import me.lecter.whitealbum.client.RiotClient
import me.lecter.whitealbum.client.ValorantAPI
import me.lecter.whitealbum.client.body.LoginData
import me.lecter.whitealbum.client.exceptions.RiotException
import me.lecter.whitealbum.server.service.account.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(
    val accountService: AccountService
) {

    @RequestMapping(value = ["/accounts"], method = [RequestMethod.GET])
    fun accounts(): ResponseEntity<List<String>> {
        return ResponseEntity.ok(accountService.getUsers())
    }

    @RequestMapping(value = ["/accounts"], method = [RequestMethod.POST])
    fun login(
        @RequestBody body: AccountRequestBody,
    ): ResponseEntity<AccountResponseBody> {
        val storeFront = accountService.login(body.username)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AccountResponseBody("failed to login", null))
        return ResponseEntity.ok(
            AccountResponseBody(
                "success to login",
                LoginData(
                    storeFront.username,
                    storeFront.singleItemOffers!!,
                    storeFront.nightMarket!!,),
            )
        )
    }
}
