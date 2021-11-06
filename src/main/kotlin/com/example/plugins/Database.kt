package com.example.plugins

import com.example.database.tables.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

fun configureDatabase() {
    Database.connect(hikari())
    transaction {
        create(Users, Comments, Rates, Applications, Subjects, Requests, MentorsSubjects, UsersMentors)

        val user = UserDao.new {
            name = "User"
            surname = ""
            course = 3
            specialty = ""
            isMentor = 0
            email = "user@gmail.com"
            password = "12345"
            registerDate = System.currentTimeMillis()
        }

        val mentor = UserDao.new {
            name = "Mentor"
            surname = ""
            course = 3
            specialty = ""
            isMentor = 1
            email = "mentor@gmail.com"
            password = "12345"
            registerDate = System.currentTimeMillis()
        }

        SubjectDao.new {
            name = "Системи штучного інтелекту"
            course = 3
        }
        SubjectDao.new {
            name = "Проектування інформаційних систем"
            course = 3
        }

        RateDao.new {
            rate = 5
            userId = user.id.value
            mentorId = mentor.id.value
        }

        ApplicationDao.new {
            this.user = user
            this.mentor = mentor
            comment = "HELP"
            date = System.currentTimeMillis()
            subjectId = 1
        }

        ApplicationDao.new {
            this.user = user
            this.mentor = mentor
            comment = "SOS"
            date = System.currentTimeMillis()
            subjectId = 2
        }

        RequestDao.new {
            this.user = user
            comment = "HELP"
            date = System.currentTimeMillis()
            subjectId = 1
        }

        RequestDao.new {
            this.user = user
            comment = "SOS"
            date = System.currentTimeMillis()
            subjectId = 2
        }

        CommentDao.new {
            comment = "123"
            this.user = user
            this.mentor = mentor
            date = System.currentTimeMillis()
        }
        CommentDao.new {
            comment = "123"
            this.user = user
            this.mentor = mentor
            date = System.currentTimeMillis()
        }
        CommentDao.new {
            comment = "123"
            this.user = user
            this.mentor = mentor
            date = System.currentTimeMillis()
        }
    }
}

private fun hikari(): HikariDataSource {
    val config = HikariConfig()
    config.driverClassName = "org.h2.Driver"
    config.jdbcUrl = "jdbc:h2:mem:test"
    config.maximumPoolSize = 3
    config.isAutoCommit = false
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()
    return HikariDataSource(config)
}