package com.example.routes

import com.example.database.model.Resources
import com.example.database.services.SubjectService
import com.example.model.requests.Error
import com.example.model.requests.SubscribeToSubjectBody
import com.example.res.StringRes
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.subjects(subjectService: SubjectService){



    get ("/getSubjects"){
        val resource = subjectService.getSubjects()
        if (resource is Resources.Success){
            call.respond(HttpStatusCode.OK,resource.data?: emptyList())
        }else{
            call.respond(HttpStatusCode.BadRequest, resource.message?:"")
        }
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