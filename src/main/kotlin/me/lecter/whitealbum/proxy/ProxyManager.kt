package me.lecter.whitealbum.proxy

import java.io.File

class ProxyManager {
    /*
    companion object {
        private var next = 0
        private const val FILE_NAME = "proxy.txt"
        var proxyList: MutableList<ProxyServer> = getProxyList()

        fun getNext(): ProxyServer =
            proxyList[next()]

        private fun next(): Int {
            next++
            if (next > proxyList.size - 1) {
                next = 0
            }
            return next
        }

        private fun getProxyList(): MutableList<ProxyServer> {
            val proxyFile = File(FILE_NAME)
            if (proxyFile.exists().not()) {
                makeProxyListFile()
                return mutableListOf()
            }
            val proxyList = mutableListOf<ProxyServer>()
            proxyFile.readLines().forEach {
                val ipAddressAndPort = it.split(":")
                if (ipAddressAndPort.size == 2) {
                    // TODO validation check here
                    proxyList.add(ProxyServer(ipAddressAndPort[0], Integer.parseInt(ipAddressAndPort[1])))
                }
            }
            return proxyList
        }

        private fun makeProxyListFile(): Boolean =
            File(FILE_NAME).createNewFile()
    }
     */
}