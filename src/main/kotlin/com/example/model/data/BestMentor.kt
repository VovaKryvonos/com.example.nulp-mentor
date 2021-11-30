package com.example.model.data

import kotlinx.serialization.Serializable

@Serializable
data class BestMentor(
    val mentor: Mentor,
    val subject: SubjectWithoutMentors
)
