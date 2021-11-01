package com.example.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val message: String="",
    val code: Int
)
