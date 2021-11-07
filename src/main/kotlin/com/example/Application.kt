package com.example

import com.example.plugins.*
import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.launch

fun main(args: Array<String>): Unit =
    EngineMain.main(args)

fun Application.module() {
    configureRouting()
    configureSerialization()
    configureHTTP()
    configureDatabase()
    this.launch { configureScheduler() }
}