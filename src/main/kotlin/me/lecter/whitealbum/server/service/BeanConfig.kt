package me.lecter.whitealbum.server.service

import me.lecter.whitealbum.server.service.account.AccountService
import me.lecter.whitealbum.server.service.account.AccountServiceImpl
import me.lecter.whitealbum.server.service.login.LoginService
import me.lecter.whitealbum.server.service.login.LoginServiceImpl
import me.lecter.whitealbum.server.service.settings.SettingService
import me.lecter.whitealbum.server.service.settings.SettingServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfig {

    @Bean
    fun loginService() : LoginService {
        return LoginServiceImpl()
    }

    @Bean
    fun accountService() : AccountService {
        return AccountServiceImpl()
    }

    @Bean
    fun settingService(): SettingService {
        return SettingServiceImpl()
    }
}