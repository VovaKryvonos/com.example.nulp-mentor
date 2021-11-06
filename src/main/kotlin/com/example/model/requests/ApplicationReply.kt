package com.example.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationReply(
    val applicationId: Int,
    val comment: String,
    val accepted: Boolean
)
