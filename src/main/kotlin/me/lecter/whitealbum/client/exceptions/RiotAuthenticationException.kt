package me.lecter.whitealbum.client.exceptions

class RiotAuthenticationException(message: String = "Failed to authenticate. Make sure username and password are correct") :
    RiotException(message)
