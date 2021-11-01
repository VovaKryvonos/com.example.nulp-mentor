package com.example.database.tables

import com.example.model.data.Application
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Applications: IntIdTable() {
    val user = reference("user",Users)
    val mentor = reference("mentor", Users)
    val subjectId = integer("subjectId")
    val date = long("date")
    val comment = varchar("comment", 512)
}

class ApplicationDao(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<ApplicationDao>(Applications)
    var user by UserDao referencedOn Applications.user
    var mentor by UserDao referencedOn Applications.mentor
    var subjectId by Applications.subjectId
    var date by Applications.date
    var comment by Applications.comment

    fun toApplication() = Application(
        userId = user.id.value,
        mentorId = mentor.id.value,
        subjectId = subjectId,
        date = date,
        comment = comment,
        id = id.value
    )
}