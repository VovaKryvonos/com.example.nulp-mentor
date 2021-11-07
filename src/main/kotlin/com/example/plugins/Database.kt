package com.example.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase() {
    val user = environment.config.property("database.user").getString()
    val password = environment.config.property("database.password").getString()
    Database.connect(hikari(user,password))
}

private fun hikari(user: String, password: String): HikariDataSource {
    val config = HikariConfig()
    config.driverClassName = "org.postgresql.Driver"
    config.jdbcUrl = "jdbc:postgresql://localhost:5432/nulp_mentor"
    config.password = password
    config.username = user
    config.maximumPoolSize = 3
    config.isAutoCommit = false
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()
    return HikariDataSource(config)
}