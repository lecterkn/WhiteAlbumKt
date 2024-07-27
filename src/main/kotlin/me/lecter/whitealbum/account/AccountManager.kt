package me.lecter.whitealbum.account

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileWriter
import java.io.IOException

class AccountManager {
    private val FILE_NAME = "saved"
    var accounts = mutableListOf<RiotAccount>()

    fun load(): Boolean {
        val file = File(FILE_NAME)
        if (!file.exists()) {
            println("\"saved\" does not exists.")
            save()
            return false
        }
        val body = file.readText()
        if (body.isEmpty()) {
            println("failed to load accounts.")
            return false
        }
        accounts = Json.decodeFromString(body)
        println("loaded accounts")
        for (account in accounts) {
            println(account.username)
        }
        return true
    }

    fun save(): Boolean {
        for (account in accounts) {
            println(account.username)
        }
        try {
            val json =
                Json {
                    encodeDefaults = true
                    prettyPrint = true
                }
            FileWriter(FILE_NAME).use { writer ->
                writer.write(json.encodeToString(accounts))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return true
    }

    fun save(
        username: String,
        password: String,
    ): Boolean {
        return save(RiotAccount(username, password))
    }

    fun save(account: RiotAccount): Boolean {
        add(account)
        return save()
    }

    fun add(account: RiotAccount) {
        for (acc in accounts) {
            if (acc.username == account.username) {
                acc.password = account.password
                return
            }
        }
        accounts.add(account)
    }
}
