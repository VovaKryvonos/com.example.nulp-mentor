package com.example.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationBody(
    val userId: Int,
    val mentorId: Int,
    val subjectId: Int,
    val date: Long,
    val comment: String
)
