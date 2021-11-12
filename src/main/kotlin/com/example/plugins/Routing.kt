package com.example.plugins

import com.example.database.services.ApplicationService
import com.example.database.services.RateService
import com.example.database.services.SubjectService
import com.example.database.services.UserService
import com.example.notification.OneSignalServiceImpl
import com.example.routes.applications
import com.example.routes.auth
import com.example.routes.rate
import com.example.routes.subjects
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.routing.*

fun Application.configureRouting() {
    routing {
        val userService = UserService()
        val rateService = RateService()
        val subjectService = SubjectService()
        val applicationService = ApplicationService()
        val client = HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
        val apiKey = environment.config.property("onesignal.api_key").getString()
        val notificationService = OneSignalServiceImpl(client, apiKey)
        auth(userService)
        rate(rateService)
        subjects(subjectService)
        applications(applicationService,notificationService)
    }
}
