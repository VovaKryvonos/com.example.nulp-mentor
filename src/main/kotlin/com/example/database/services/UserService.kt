package com.example.database.services

import com.example.database.model.Resources
import com.example.database.tables.UserDao
import com.example.database.tables.Users
import com.example.model.data.Mentor
import com.example.model.data.User
import com.example.model.data.UserAllData
import com.example.res.StringRes
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserService {

    suspend fun getAllUsers(): List<UserAllData> = newSuspendedTransaction {
        UserDao.all().map { it.toUserAllData() }
    }

    suspend fun getMentors(): List<Mentor> = newSuspendedTransaction {
        UserDao.find { Users.isMentor eq 1 }.map { it.toMentor() }
    }

    suspend fun getMentor(mentorId: Int): Resources<Mentor> = newSuspendedTransaction {
        try {
            Resources.Success(UserDao[mentorId].toMentor())
        } catch (e: Exception){
            Resources.Error("")
        }
    }

    suspend fun addUser(user: User, pass: String, token: String): Resources<User> = newSuspendedTransaction {
        UserDao.all().toList()
        val userRow = UserDao.find { Users.email eq user.email }.firstOrNull()
        if (userRow != null) {
            return@newSuspendedTransaction Resources.Error(StringRes.userAlreadyExists)
        } else {
            with(UserDao.new {
                name = user.name
                surname = user.surname
                email = user.email
                course = user.course
                specialty = user.specialty
                isMentor = if (user.isMentor) 1 else 0
                registerDate = user.registerDate
                password = pass
                this.token = token
            }) {
                return@newSuspendedTransaction Resources.Success(toUser())
            }
        }
    }

    suspend fun login(email: String, pass: String, token: String): Resources<User> = newSuspendedTransaction {
        val user = UserDao.find { Users.email eq email }.firstOrNull()
            ?: return@newSuspendedTransaction Resources.Error(StringRes.userNotExists)
        if (user.password == pass) {
            user.token = token
            return@newSuspendedTransaction Resources.Success(user.toUser())
        } else {
            return@newSuspendedTransaction Resources.Error(StringRes.wrongPass)
        }
    }

    suspend fun becomeMentor(mentorId: Int) : Resources<Mentor> = newSuspendedTransaction {
        try {
            with(UserDao[mentorId]){
                isMentor = 1
                Resources.Success(toMentor())
            }
        } catch (e: Exception){
            Resources.Error("")
        }
    }
}