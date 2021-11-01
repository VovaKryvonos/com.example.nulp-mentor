package com.example.database.services

import com.example.database.model.Resources
import com.example.database.tables.UserDao
import com.example.database.tables.Users
import com.example.model.data.User
import com.example.res.StringRes
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserService {

    suspend fun getAllUsers(): List<User> = newSuspendedTransaction {
        UserDao.all().map { it.toUser() }
    }

    suspend fun getMentors(): List<User> = newSuspendedTransaction {
        UserDao.find { Users.isMentor eq 1 }.map { it.toUser() }
    }

    suspend fun addUser(user: User, pass: String): Resources<User> = newSuspendedTransaction {
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
            }) {
                return@newSuspendedTransaction Resources.Success(toUser())
            }
        }
    }

    suspend fun login(email: String, pass: String): Resources<User> = newSuspendedTransaction {
        val user = UserDao.find { Users.email eq email }.firstOrNull()
            ?: return@newSuspendedTransaction Resources.Error(StringRes.userNotExists)
        if (user.password == pass) {
            return@newSuspendedTransaction Resources.Success(user.toUser())
        } else {
            return@newSuspendedTransaction Resources.Error(StringRes.wrongPass)
        }
    }
}