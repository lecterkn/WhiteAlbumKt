package me.lecter.whitealbum.client

import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import java.net.Socket
import java.security.*
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*

class RiotSSL {

    companion object {

        val protocols = arrayOf("TLSv1.2", "TLSv1.3")

        val CIPHER = arrayOf(
            "TLS_CHACHA20_POLY1305_SHA256",
            "TLS_AES_128_GCM_SHA256",
            "TLS_AES_256_GCM_SHA384",
        )

        val NAMED_GROUPS = arrayOf("x25519", "secp256r1", "secp384r1")

        val algorithms = listOf(
            "ecdsa_secp256r1_sha256",
            "rsa_pss_rsae_sha256",
            "rsa_pkcs1_sha256",
            "ecdsa_secp384r1_sha384",
            "rsa_pss_rsae_sha384",
            "rsa_pkcs1_sha384",
            "rsa_pss_rsae_sha512",
            "rsa_pkcs1_sha512",
            "rsa_pkcs1_sha1"
        )

        private val algorithmConstraints = object : AlgorithmConstraints {
            override fun permits(
                primitives: Set<CryptoPrimitive>,
                algorithm: String,
                parameters: AlgorithmParameters?
            ): Boolean {
                return algorithms.contains(algorithm)
            }

            override fun permits(
                primitives: Set<CryptoPrimitive>,
                key: java.security.Key?
            ): Boolean {
                return true // 特定の鍵に関する制約がない場合
            }

            override fun permits(
                primitives: MutableSet<CryptoPrimitive>?,
                algorithm: String?,
                key: Key?,
                parameters: AlgorithmParameters?,
            ): Boolean {
                return algorithms.contains(algorithm)
            }
        }

        fun createSSLContext(): SSLContext {
            val trustManager = object : X509TrustManager {
                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            }
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
            return sslContext
        }

        fun createSSLParameters(): SSLParameters {
            val sslParameters = SSLParameters()

            sslParameters.cipherSuites = CIPHER
            sslParameters.namedGroups = NAMED_GROUPS
            sslParameters.algorithmConstraints = algorithmConstraints

            return sslParameters
        }

        fun createSSLSocketFactory(): SSLConnectionSocketFactory {
            Security.setProperty("jdk.tls.disabledAlgorithms", "SSL_OP_NO_ENCRYPT_THEN_MAC,SSL_OP_NO_TICKET")

            val sslContext = createSSLContext()
            val sslParameters = createSSLParameters()
            val sslSocketFactory = object : SSLConnectionSocketFactory(sslContext) {
                override fun prepareSocket(socket: SSLSocket) {
                    socket.sslParameters = sslParameters
                    socket.enabledProtocols = arrayOf("TLSv1.3", "TLSv1.2")
                }
            }
            return sslSocketFactory
        }

        fun createRiotAuthSSLContext(): SSLContext {
            // Create SSLContext
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, null, null)

            // Set minimum TLS version to TLSv1
            sslContext.defaultSSLParameters.protocols = arrayOf("TLSv1")



            // Set ciphersuites
            val ciphers13 = "TLS_AES_128_GCM_SHA256:TLS_AES_256_GCM_SHA384"
            val ciphers = "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256:TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384"

            sslContext.defaultSSLParameters.cipherSuites = ciphers13.split(":").toTypedArray()

            // Setting SSL_CTRL_SET_SIGALGS_LIST and SSL_CTRL_SET_GROUPS_LIST
            // Note: This part is highly platform and implementation-specific and requires access to native SSL libraries.

            // Uncomment and customize if using native library access
            // val nativeLibrary = System.loadLibrary("ssl")
            // nativeLibrary.SSL_CTX_set_ciphersuites(sslContext, ciphers13)
            // nativeLibrary.SSL_CTX_set_cipher_list(sslContext, ciphers)
            // nativeLibrary.SSL_CTX_ctrl(sslContext, 98, 0, "SIGALGS")
            // nativeLibrary.SSL_CTX_ctrl(sslContext, 92, 0, "x25519:secp256r1:secp384r1")

            return sslContext
        }
    }

}