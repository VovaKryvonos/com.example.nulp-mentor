package com.example.database.services

import com.example.database.model.Resources
import com.example.database.tables.*
import com.example.model.data.Application
import com.example.model.data.ApplicationData
import com.example.model.data.MentorsReply
import com.example.model.data.MentorsRequestReply
import com.example.model.requests.*
import com.example.res.StringRes
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ApplicationService {

    suspend fun makeAnApplication(applicationBody: ApplicationBody): Resources<String> = newSuspendedTransaction {
        try {
            val user = UserDao[applicationBody.userId]
            val mentor = UserDao[applicationBody.mentorId]
            user.userApplications.map { it.toApplication() }.forEach {
                if (it.mentorId == mentor.id.value && it.subjectId == applicationBody.subjectId &&
                    (it.state == Applications.STATE_ACTIVE || it.state == Applications.STATE_ACCEPTED)
                ) {
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

    suspend fun makeMentorRequest(body: MentorRequestBody): Resources<Void?> = newSuspendedTransaction {
        try {
            val user = UserDao[body.userId]
            val subject = SubjectDao[body.subjectId]
            user.requests.map { it.toRequest() }.forEach {
                if (it.subjectId == subject.id.value &&
                    (it.state == Requests.STATE_ACTIVE || it.state == Requests.STATE_ACCEPTED)
                ) {
                    return@newSuspendedTransaction Resources.Error(StringRes.applicationAlreadyExist)
                }
            }
            RequestDao.new {
                this.user = user
                this.subject = subject
                this.date = body.date
                this.comment = body.comment
            }
            Resources.Success(null)
        } catch (e: Exception) {
            Resources.Error(StringRes.somethingWentWrong)
        }
    }

    suspend fun replyToMentorRequest(body: MentorRequestReplyBody): Resources<MentorsRequestReply> =
        newSuspendedTransaction {
            try {
                val request = RequestDao[body.requestId]
                val token = request.user.token
                val mentor = UserDao[body.mentorId]
                val message = String.format(StringRes.mentorsReplyMsg, mentor.name, mentor.surname)
                val mentorsReply = MentorsRequestReply(
                    pushMessage = message,
                    token = token ?: "",
                    requestId = request.id.value,
                    comment = request.comment,
                    mentorsEmail = mentor.email,
                )
                UserMentorDao.new {
                    this.menteeId = request.user.id.value
                    this.mentorId = mentor.id.value
                    this.subjectId = request.subject.id.value
                }
                request.state = Requests.STATE_ACCEPTED
                Resources.Success(mentorsReply)
            } catch (e: Exception) {
                Resources.Error(StringRes.somethingWentWrong)
            }
        }

    suspend fun cancelMentorRequest(body: CancelMentorRequestBody): Resources<Void?> = newSuspendedTransaction {
        try {
            val request = RequestDao[body.requestId]
            request.state = Requests.STATE_CANCELED
            Resources.Success(null)
        } catch (e: Exception) {
            Resources.Error(StringRes.somethingWentWrong)
        }
    }

    suspend fun checkRequests() = newSuspendedTransaction {
        val oneWeek = 1000 * 60 * 60 * 24 * 7
        for (request in RequestDao.all()){
            if (System.currentTimeMillis()-request.date>4L*oneWeek){
                request.delete()
                continue
            }
            if (System.currentTimeMillis()-request.date>oneWeek){
                request.state = Requests.STATE_EXPIRED
            }
        }
    }

    suspend fun checkApplications() = newSuspendedTransaction {
        val oneWeek = 1000 * 60 * 60 * 24 * 7
        for (application in ApplicationDao.all()){
            if (System.currentTimeMillis()-application.date>4L*oneWeek){
                application.delete()
                continue
            }
            if (System.currentTimeMillis()-application.date>oneWeek){
                application.state = Requests.STATE_EXPIRED
            }
        }
    }

    suspend fun getApplications(ids: List<Int>) = newSuspendedTransaction {
        try {
            println(ids.toString())
            val applications = arrayListOf<ApplicationData>()
            ids.forEach{id->
                applications.add(ApplicationDao[id].toApplicationData())
            }
            Resources.Success(applications)
        } catch (e: Exception) {
            Resources.Error(StringRes.somethingWentWrong)
        }
    }


}
