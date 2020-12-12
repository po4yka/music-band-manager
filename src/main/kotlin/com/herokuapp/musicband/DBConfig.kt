package com.herokuapp.musicband

import com.herokuapp.musicband.data.Groups
import com.herokuapp.musicband.data.Performers
import com.herokuapp.musicband.data.Songs
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

const val HIKARI_CONFIG_KEY = "ktor.hikariconfig"

@KtorExperimentalAPI
fun Application.initDB() {
    val configPath = environment.config.property(HIKARI_CONFIG_KEY).getString()
    val dbConfig = HikariConfig(configPath)
    val dataSource = HikariDataSource(dbConfig)
    Database.connect(dataSource)
    transaction {
        addLogger(StdOutSqlLogger)
    }
    createTables()
    println("Initialized Database")
}

private fun createTables() = transaction {
    SchemaUtils.create(
        Groups,
        Performers,
        Songs
    )
}
