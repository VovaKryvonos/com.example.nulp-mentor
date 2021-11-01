package com.example.model.data

import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val id: Int,
    val name: String,
    val course: Int,
    val mentors: List<User>
)