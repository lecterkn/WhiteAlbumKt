package me.lecter.whitealbum.utils

class OSUtils {
    companion object {
        val OS: OSType

        init {
            val osName = System.getProperty("os.name").lowercase()
            OS =
                when {
                    osName.startsWith("win") -> OSType.WINDOWS
                    osName.startsWith("mac") -> OSType.MAC
                    else -> OSType.LINUX
                }
        }
    }
}

enum class OSType {
    WINDOWS,
    MAC,
    LINUX,
}
