package com.example.database.services

import com.example.database.model.Resources
import com.example.database.tables.SubjectDao
import com.example.database.tables.UserDao
import com.example.model.data.Subject
import com.example.model.requests.SubscribeToSubjectBody
import com.example.res.StringRes
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.lang.Exception

class SubjectService {

    suspend fun getSubjects(): List<Subject> = newSuspendedTransaction {
        SubjectDao.all().map {
            it.toSubject().also { subject ->
                subject.mentors = it.mentors.map { user -> user.toUser() }
            }
        }
    }

    suspend fun subscribe(body: SubscribeToSubjectBody): Resources<Void?> = newSuspendedTransaction {
        try {
            val subject = SubjectDao[body.subjectId]
            val mentor = UserDao[body.mentorId]
            val subjects = arrayListOf(subject)
            subjects.addAll(mentor.subjects)
            mentor.subjects = SizedCollection(subjects)
            return@newSuspendedTransaction Resources.Success(null)
        }catch (e: Exception){
            print(e.localizedMessage)
            return@newSuspendedTransaction Resources.Error(StringRes.somethingWentWrong)
        }

    }

}