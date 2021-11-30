package com.example.model.requests

import com.example.model.data.User
import kotlinx.serialization.Serializable

@Serializable
data class RegisterBody(
    val name: String,
    val surname: String,
    val password: String,
    val email: String,
    val course: Int,
    val specialty: String,
    val isMentor: Boolean,
    val token: String
)

fun RegisterBody.toUser(): User {
    return User(
        name = name,
        surname = surname,
        email = email,
        course = course,
        specialty = specialty,
        isMentor = isMentor,
        registerDate = System.currentTimeMillis(),
    )
}
