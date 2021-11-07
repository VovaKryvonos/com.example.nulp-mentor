package com.example.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class MentorRequestReplyBody(
    val mentorId: Int,
    val requestId: Int,
    val comment: String,
    val date: Long
)
