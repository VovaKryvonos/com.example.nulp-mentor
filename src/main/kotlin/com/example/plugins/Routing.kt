package com.example.plugins

import com.example.database.services.RateService
import com.example.database.services.SubjectService
import com.example.database.services.UserService
import com.example.routes.auth
import com.example.routes.rate
import com.example.routes.subjects
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {
    routing {
        val userService = UserService()
        val rateService = RateService()
        val subjectService = SubjectService()
        auth(userService)
        rate(rateService)
        subjects(subjectService)
    }
}
