package com.example.model.data

import kotlinx.serialization.Serializable

@Serializable
data class Application(
    val userId : Int,
    val mentorId : Int,
    val subjectId: Int,
    val date: Long,
    val comment: String,
    val id: Int
)