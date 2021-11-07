package com.example.model.data

data class MentorsRequestReply(
    val pushMessage: String,
    val token: String,
    val requestId: Int,
    val comment: String,
    val mentorsEmail: String
)
