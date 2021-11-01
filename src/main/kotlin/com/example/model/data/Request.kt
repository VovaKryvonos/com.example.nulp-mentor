package com.example.model.data

import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val id: Int,
    val userId: Int,
    val date: Long,
    val subjectId: Int,
    val comment: String
)