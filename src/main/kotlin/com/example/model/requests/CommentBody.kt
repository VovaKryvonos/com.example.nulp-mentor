package com.example.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class CommentBody(
    val comment: String="",
    val userId : Int,
    val mentorId : Int,
    val date : Long
)