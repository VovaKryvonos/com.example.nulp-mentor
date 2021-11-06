package com.example.database.tables

import com.example.model.data.Application
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Applications: IntIdTable() {
    const val STATE_ACTIVE = 0
    const val STATE_EXPIRED = 1
    const val STATE_ACCEPTED = 2
    const val STATE_REJECTED = 3

    val user = reference("user",Users)
    val mentor = reference("mentor", Users)
    val subjectId = integer("subjectId")
    val date = long("date")
    val comment = varchar("comment", 512)
    val state = integer("sate").default(STATE_ACTIVE)
}

class ApplicationDao(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<ApplicationDao>(Applications)
    var user by UserDao referencedOn Applications.user
    var mentor by UserDao referencedOn Applications.mentor
    var subjectId by Applications.subjectId
    var date by Applications.date
    var comment by Applications.comment
    var state by Applications.state

    fun toApplication() = Application(
        userId = user.id.value,
        mentorId = mentor.id.value,
        subjectId = subjectId,
        date = date,
        comment = comment,
        id = id.value
    )
}