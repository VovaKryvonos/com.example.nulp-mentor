package com.example.routes

import com.example.database.model.Resources
import com.example.database.services.UserService
import com.example.model.data.User
import com.example.model.requests.Error
import com.example.model.requests.LoginBody
import com.example.model.requests.RegisterBody
import com.example.model.requests.toUser
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.auth(userService: UserService) {

    route("/users"){
        get {
            call.respond(HttpStatusCode.OK, userService.getAllUsers())
        }
    }

    route("/signin") {
        post("/email") {
            val body = call.receive<LoginBody>()
            println(body)
            val result = userService.login(body.email, body.password)
            if (result is Resources.Error){
                call.respond(HttpStatusCode.Forbidden, Error(result.message?:"", HttpStatusCode.Forbidden.value))
            }else{
                call.respond(HttpStatusCode.OK, result.data?:User())
            }
        }
    }

    route("/signup") {
        post("/email") {
            val body = call.receive<RegisterBody>()
            val result = userService.addUser(body.toUser(), body.password)
            if (result is Resources.Error){
                call.respond(HttpStatusCode.Conflict, Error(result.message?:"", HttpStatusCode.Conflict.value))
            }else{
                call.respond(HttpStatusCode.OK, result.data?:User())
            }
        }
    }
}