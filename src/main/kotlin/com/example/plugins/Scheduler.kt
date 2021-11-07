package com.example.plugins

import kotlinx.coroutines.delay

suspend fun configureScheduler(){
    while (true) {
        delay(1000 * 15)
        print("configureScheduler")
    }
}