package com.example.routes

import com.example.database.model.Resources
import com.example.database.services.UserService
import com.example.model.data.Mentor
import com.example.model.requests.Error
import com.example.res.StringRes
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.mentor(userService: UserService) {
    get("/mentor/{mentorId}") {
        val id = try {
            call.parameters["mentorId"]?.toInt()
        } catch (e: NumberFormatException) {
            call.respond(HttpStatusCode.NotFound, Error(StringRes.somethingWentWrong, HttpStatusCode.NotFound.value))
            return@get
        }
        try {
            val mentor = userService.getMentor(id ?: -1)

            if (mentor is Resources.Success) {
                call.respond(HttpStatusCode.OK, mentor.data ?: Mentor())
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    Error(StringRes.somethingWentWrong, HttpStatusCode.NotFound.value)
                )
            }
        } catch (e: Exception) {
            print(e.localizedMessage)
            call.respond(HttpStatusCode.NotFound, Error(StringRes.somethingWentWrong, HttpStatusCode.NotFound.value))
        }
    }

    get("/mentors") {
        call.respond(HttpStatusCode.OK, userService.getMentors())
    }

    put("/becomeMentor/{mentorId}") {
        val id = try {
            call.parameters["mentorId"]?.toInt()
        } catch (e: NumberFormatException) {
            call.respond(HttpStatusCode.NotFound, Error(StringRes.somethingWentWrong, HttpStatusCode.NotFound.value))
            return@put
        }
        try {
            val mentor = userService.becomeMentor(id ?: -1)

            if (mentor is Resources.Success) {
                call.respond(HttpStatusCode.OK, mentor.data ?: Mentor())
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    Error(StringRes.somethingWentWrong, HttpStatusCode.NotFound.value)
                )
            }
        } catch (e: Exception) {
            print(e.localizedMessage)
            call.respond(HttpStatusCode.NotFound, Error(StringRes.somethingWentWrong, HttpStatusCode.NotFound.value))
        }
    }
}
