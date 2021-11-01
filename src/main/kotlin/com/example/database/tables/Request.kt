package com.example.database.tables

import com.example.model.data.Request
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Requests : IntIdTable() {
    val user = reference("user", Users)
    val date = long("date")
    val subjectId = integer("subjectId")
    val comment = varchar("comment", 512)
}

class RequestDao(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<RequestDao>(Requests)
    var user by UserDao referencedOn Requests.user
    var date by Requests.date
    var subjectId by Requests.subjectId
    var comment by Requests.comment

    fun toRequest() = Request(
        id = id.value,
        userId = user.id.value,
        date = date,
        subjectId = subjectId,
        comment = comment
    )
}