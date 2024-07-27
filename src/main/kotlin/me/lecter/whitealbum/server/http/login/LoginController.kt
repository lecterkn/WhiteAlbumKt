package me.lecter.whitealbum.server.http.login

import me.lecter.whitealbum.client.body.LoginData
import me.lecter.whitealbum.server.service.login.LoginService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
    val loginService: LoginService,
) {
    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    fun login(
        @RequestBody body: LoginRequestBody,
    ): ResponseEntity<LoginResponseBody> {
        val storeFront = loginService.login(body.username, body.password, body.remember)

        if (storeFront?.singleItemOffers == null || storeFront.nightMarket == null) {
            println("storeFront error")
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(LoginResponseBody("failed to logging in", null))
        }

        return ResponseEntity.ok(
            LoginResponseBody(
                "success to login",
                LoginData(storeFront.username, storeFront.singleItemOffers!!, storeFront.nightMarket!!),
            ),
        )
    }
}
