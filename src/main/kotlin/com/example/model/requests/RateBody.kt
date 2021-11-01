package com.example.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class RateBody(
    val rate: Int,
    val userId: Int,
    val mentorId: Int
)
