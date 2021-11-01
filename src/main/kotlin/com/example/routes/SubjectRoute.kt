package com.example.routes

import com.example.database.model.Resources
import com.example.database.services.SubjectService
import com.example.model.requests.SubscribeToSubjectBody
import com.example.res.StringRes
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.subjects(subjectService: SubjectService){

    get ("/getSubjects"){
        call.respond(HttpStatusCode.OK, subjectService.getSubjects())
    }

    get ("/getSubjects/{id}"){
        val id = try {
            call.parameters["id"]?.toInt()
        }catch (e:NumberFormatException ){
            return@get call.respond(HttpStatusCode.NotFound, StringRes.somethingWentWrong)
        }
        call.respond(HttpStatusCode.OK, subjectService.getSubjects())
    }

    post("/subscribe") {
        val body = call.receive<SubscribeToSubjectBody>()
        val resource = subjectService.subscribe(body)
        if (resource is Resources.Success){
            call.respond(HttpStatusCode.OK)
        }else{
            call.respond(HttpStatusCode.BadRequest, resource.message?:"")
        }
    }
}