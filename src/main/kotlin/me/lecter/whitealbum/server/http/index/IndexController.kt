package me.lecter.whitealbum.server.http.index

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class IndexController {
    @RequestMapping(value = ["/", "/index.html", "/lecterkn/"], method = [RequestMethod.GET])
    fun get(): String {
        return "index.html"
    }
}
