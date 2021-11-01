package com.example.model.data

import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val id: Int,
    val name: String,
    val course: Int,
    var mentors: List<User> = emptyList()
)

@Serializable
data class SubjectWithoutMentors(
    val id:Int,
    val name: String,
    val course: Int
)