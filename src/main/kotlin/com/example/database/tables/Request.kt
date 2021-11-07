package com.example.database.tables

import com.example.model.data.Request
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Requests : IntIdTable() {

    const val STATE_ACTIVE = 0
    const val STATE_EXPIRED = 1
    const val STATE_ACCEPTED = 2
    const val STATE_CANCELED = 3

    val user = reference("user", Users)
    val subject = reference("subject", Subjects)
    val date = long("date")
    val comment = varchar("comment", 512)
    val state = integer("state").default(STATE_ACTIVE)
}

class RequestDao(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<RequestDao>(Requests)
    var user by UserDao referencedOn Requests.user
    var date by Requests.date
    var subject by SubjectDao referencedOn Requests.subject
    var comment by Requests.comment
    var state by Requests.state
    fun toRequest() = Request(
        id = id.value,
        userId = user.id.value,
        date = date,
        subjectId = subject.id.value,
        comment = comment,
        state = state
    )
}