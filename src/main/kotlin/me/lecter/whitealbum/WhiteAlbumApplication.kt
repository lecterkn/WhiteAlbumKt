package me.lecter.whitealbum

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WhiteAlbumApplication

fun main(args: Array<String>) {
    WhiteAlbum.pre()
    runApplication<WhiteAlbumApplication>(*args)
    WhiteAlbum.post()
}
