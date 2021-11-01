package com.example.routes

import com.example.database.services.SubjectService
import com.example.model.requests.SubscribeToSubjectBody
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.subjects(subjectService: SubjectService){

    get ("/getSubjects"){
        call.respond(HttpStatusCode.OK, subjectService.getSubjects())
    }

    post("/subscribe") {
        val body = call.receive<SubscribeToSubjectBody>()

    }
}