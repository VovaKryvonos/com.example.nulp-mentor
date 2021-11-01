package com.example.database.tables

import com.example.model.data.Comment
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Comments: IntIdTable() {
    val comment = varchar("comment",255)
    val user = reference("userId", Users)
    val mentor = reference("mentor", Users)
    val date = long("date")
}

class CommentDao(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<CommentDao>(Comments)
    var comment by Comments.comment
    var user by UserDao referencedOn Comments.user
    var mentor by UserDao referencedOn Comments.mentor
    var date by Comments.date

    fun toComment() = Comment(
        id = id.value,
        comment = comment,
        user = user.name + " " + user.surname,
        date = date
    )
}