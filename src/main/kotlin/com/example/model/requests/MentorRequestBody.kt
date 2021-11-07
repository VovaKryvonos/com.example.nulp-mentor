package com.example.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class MentorRequestBody(
    val userId: Int,
    val comment: String,
    val subjectId: Int,
    val date: Long
)
