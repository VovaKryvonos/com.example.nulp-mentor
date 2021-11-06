package com.example.database.services

import com.example.database.model.Resources
import com.example.database.tables.*
import com.example.model.data.Application
import com.example.model.data.MentorsReply
import com.example.model.requests.ApplicationBody
import com.example.model.requests.ApplicationReply
import com.example.res.StringRes
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ApplicationService {

    suspend fun makeAnApplication(applicationBody: ApplicationBody): Resources<String> = newSuspendedTransaction {
        try {
            val user = UserDao[applicationBody.userId]
            val mentor = UserDao[applicationBody.mentorId]
            user.userApplications.map { it.toApplication() }.forEach {
                if (it.mentorId == mentor.id.value && it.subjectId == applicationBody.subjectId) {
                    return@newSuspendedTransaction Resources.Error(StringRes.applicationAlreadyExist)
                }
            }
            ApplicationDao.new {
                this.user = user
                this.mentor = mentor
                this.date = applicationBody.date
                this.comment = applicationBody.comment
                this.subjectId = applicationBody.subjectId
            }
            return@newSuspendedTransaction Resources.Success(mentor.token ?: "")
        } catch (e: Exception) {
            e.printStackTrace()
            return@newSuspendedTransaction Resources.Error(message = StringRes.somethingWentWrong)
        }
    }

    suspend fun replyOnApplication(body: ApplicationReply): Resources<MentorsReply> = newSuspendedTransaction {
        body.applicationId
        try {
            val application = ApplicationDao[body.applicationId]
            val token = application.user.token
            val message = String.format(StringRes.mentorsReplyMsg, application.mentor.name, application.mentor.surname)
            val mentorsReply = MentorsReply(
                pushMessage = message,
                token = token ?: "",
                applicationId = application.id.value,
                comment = application.comment,
                mentorsEmail = application.mentor.email,
                accepted = body.accepted
            )
            if (body.accepted) {
                UserMentorDao.new {
                    this.menteeId = application.user.id.value
                    this.mentorId = application.mentor.id.value
                    this.subjectId = application.subjectId
                }
                application.state = Applications.STATE_ACCEPTED
            } else {
                application.state = Applications.STATE_REJECTED
            }
            return@newSuspendedTransaction Resources.Success(mentorsReply)
        } catch (e: Exception) {
            return@newSuspendedTransaction Resources.Error(StringRes.somethingWentWrong)
        }
    }

    suspend fun getMentorsApplication(id: Int): Resources<List<Application>> = newSuspendedTransaction {
        try {
            Resources.Success(UserDao[id].appeals.map { it.toApplication() })
        } catch (e: Exception) {
            Resources.Error(StringRes.somethingWentWrong)
        }
    }
}
