package com.example.model.requests

import kotlinx.serialization.Serializable

@Serializable
class LoginBody (
    val email: String,
    val password: String
)