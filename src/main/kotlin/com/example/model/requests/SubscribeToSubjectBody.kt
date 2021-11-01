package com.example.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class SubscribeToSubjectBody(
    val subjectId: Int,
    val mentorId: Int
)
