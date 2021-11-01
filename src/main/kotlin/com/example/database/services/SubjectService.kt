package com.example.database.services

import com.example.database.tables.SubjectDao
import com.example.model.data.Subject
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class SubjectService {

    suspend fun getSubjects(): List<Subject> = newSuspendedTransaction {
        SubjectDao.all().map { it.toSubject() }
    }

}