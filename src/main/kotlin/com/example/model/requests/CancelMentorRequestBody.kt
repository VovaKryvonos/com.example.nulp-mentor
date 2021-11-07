package com.example.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class CancelMentorRequestBody(
    val requestId: Int,
    val date: Long
)
