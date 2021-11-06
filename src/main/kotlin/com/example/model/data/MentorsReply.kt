package com.example.model.data

import kotlinx.serialization.Serializable

@Serializable
data class MentorsReply(
    val pushMessage: String,
    val token: String,
    val applicationId: Int,
    val comment: String,
    val mentorsEmail: String,
    val accepted: Boolean
)
