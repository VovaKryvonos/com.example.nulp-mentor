package com.example.model.data

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: Int,
    val comment: String="",
    val user :String = "",
    val date : Long
)
