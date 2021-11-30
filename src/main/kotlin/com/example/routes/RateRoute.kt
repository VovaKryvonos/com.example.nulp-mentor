package com.example.routes

import com.example.database.model.Resources
import com.example.database.services.RateService
import com.example.model.data.Rate
import com.example.model.requests.CommentBody
import com.example.model.requests.Error
import com.example.model.requests.RateBody
import com.example.res.StringRes
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.rate(rateService: RateService) {
    route("/addComment") {
        post {
            val body = call.receive<CommentBody>()
            val resources = rateService.addComment(body)
            if (resources is Resources.Error) {
                call.respond(HttpStatusCode.BadRequest, Error( resources.message ?: "", HttpStatusCode.BadRequest.value))
            } else {
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    get("/getComments/{id}") {
        val id = try {
            call.parameters["id"]?.toInt()
        }catch (e:NumberFormatException ){
            call.respond(HttpStatusCode.NotFound, Error( StringRes.somethingWentWrong, HttpStatusCode.NotFound.value))
            return@get
        }
        val resources = rateService.getComments(id!!)
        if (resources is Resources.Error){
            call.respond(HttpStatusCode.NotFound, Error(resources.message?:"", HttpStatusCode.NotFound.value))
        }else{
            call.respond(HttpStatusCode.OK, resources.data?: emptyList<CommentBody>())
        }
    }


    route("/rate") {
        post {
            val body = call.receive<RateBody>()
            val resources = rateService.rate(body)
            if (resources is Resources.Error){
                call.respond(HttpStatusCode.BadRequest, Error(resources.message?:"", HttpStatusCode.BadRequest.value))
            }else{
                call.respond(HttpStatusCode.OK, resources.data?: Rate(-1F))
            }
        }
    }

    get("/bestMentors") {
        val resources = rateService.getBestMentors()
        if (resources is Resources.Error){
            call.respond(HttpStatusCode.BadRequest, Error(resources.message?:"", HttpStatusCode.BadRequest.value))
        }else{
            call.respond(HttpStatusCode.OK, resources.data?: emptyList())
        }
    }

    route("/rates"){
        post {
            call.respond(HttpStatusCode.OK, rateService.allRates())
        }
    }
}