package me.lecter.whitealbum.server

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/**")
            .allowedOrigins("http://127.0.0.1:5500", "http://localhost:8100")
            .allowedMethods("GET", "POST", "PATCH", "PUT", "OPTION", "DELETE")
            .allowedHeaders("Content-Type")
    }
}
