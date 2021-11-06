package com.example.routes

import com.example.database.model.Resources
import com.example.database.services.ApplicationService
import com.example.model.requests.ApplicationBody
import com.example.model.requests.ApplicationReply
import com.example.res.StringRes
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.applications(applicationService: ApplicationService) {

    post("/makeAnApplication") {
        val body = call.receive<ApplicationBody>()
        val resource = applicationService.makeAnApplication(body)
        if (resource is Resources.Error){
            call.respond(HttpStatusCode.BadRequest, resource.message?:"")
            return@post
        }
        call.respond(HttpStatusCode.OK)
        //TODO:send notification
    }

    post("/replyOnApplication") {
        val body = call.receive<ApplicationReply>()
        val resource = applicationService.replyOnApplication(body)
        if (resource is Resources.Error){
            call.respond(HttpStatusCode.BadRequest, resource.message?:"")
            return@post
        }
        call.respond(HttpStatusCode.OK)
        //TODO:send notification
    }

    get ( "/applications/{mentorId}" ){
        val id = try {
            call.parameters["mentorId"]?.toInt()
        }catch (e:NumberFormatException ){
            return@get call.respond(HttpStatusCode.NotFound, StringRes.somethingWentWrong)
        }
        call.respond(HttpStatusCode.OK, applicationService.getMentorsApplication(id?:-1).data?: emptyList())
    }
}