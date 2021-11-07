package com.example.database.tables

import com.example.model.data.Mentor
import com.example.model.data.User
import com.example.model.data.User.Companion.DefaultUser
import com.example.model.data.UserAllData
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object Users : IntIdTable() {
    val name = varchar("name", 255)
    val surname = varchar("surname", 255)
    val email = varchar("email", 255)
    val course = integer("course")
    val specialty = varchar("specialty", 255)
    val isMentor = integer("isMentor")
    val registerDate = long("registerDate")
    val password = varchar("pass", 512)
    val token = varchar("token", 200).nullable()
    val rate = float("rate").default(-1F)
}

object UsersMentors : IntIdTable() {
    val mentorId = integer("mentorId")
    val menteeId = integer("menteeId")
    val subjectId = integer("subjectId")
}

class UserMentorDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserMentorDao>(UsersMentors)

    var mentorId by UsersMentors.mentorId
    var menteeId by UsersMentors.menteeId
    var subjectId by UsersMentors.subjectId

    suspend fun mentee(): UserDao? = newSuspendedTransaction {
        try {
            return@newSuspendedTransaction UserDao[menteeId]
        } catch (e: Exception) {
            return@newSuspendedTransaction null
        }
    }


    suspend fun mentor(): UserDao? = newSuspendedTransaction {
        try {
            return@newSuspendedTransaction UserDao[mentorId]
        } catch (e: Exception) {
            return@newSuspendedTransaction null
        }
    }

    suspend fun subject(): SubjectDao? = newSuspendedTransaction {
        try {
            return@newSuspendedTransaction SubjectDao[subjectId]
        } catch (e: Exception) {
            return@newSuspendedTransaction null
        }
    }

}

class UserDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDao>(Users)

    var name by Users.name
    var surname by Users.surname
    var email by Users.email
    var course by Users.course
    var specialty by Users.specialty
    var isMentor by Users.isMentor
    var registerDate by Users.registerDate
    var password by Users.password
    var rate by Users.rate
    var token by Users.token
    var subjects by SubjectDao via MentorsSubjects
    val comments by CommentDao referrersOn Comments.mentor
    val userApplications by ApplicationDao referrersOn Applications.user
    val appeals by ApplicationDao referrersOn Applications.mentor
    val requests by RequestDao referrersOn Requests.user


    fun toUser(): User {
        return User(
            id = id.value,
            name = name,
            surname = surname,
            email = email,
            course = course,
            specialty = specialty,
            isMentor = isMentor == 1,
            registerDate = registerDate,
            rate = rate
        )
    }

    suspend fun toMentor(): Mentor {
        return Mentor(
            id = id.value,
            name = name,
            surname = surname,
            email = email,
            course = course,
            specialty = specialty,
            isMentor = isMentor == 1,
            registerDate = registerDate,
            rate = rate,
            subjects = subjects.map { it.toSubjectWithoutMentors() },
            mentees = UserMentorDao.find { UsersMentors.mentorId eq id.value }.map { it.mentee()?.toUser()?: DefaultUser }
        )
    }

    fun toUserAllData(): UserAllData {
        return UserAllData(
            id = id.value,
            name = name,
            surname = surname,
            email = email,
            course = course,
            specialty = specialty,
            isMentor = isMentor == 1,
            registerDate = registerDate,
            rate = rate,
            token = token,
            comments = comments.map { it.toComment() },
            userApplications = userApplications.map { it.toApplication() },
            appeals = appeals.map { it.toApplication() },
            requests = requests.map { it.toRequest() },
            subjects = subjects.map { it.toSubjectWithoutMentors() }
        )
    }
}

