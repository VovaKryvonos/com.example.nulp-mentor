package com.example.database.services

import com.example.database.model.Resources
import com.example.database.tables.*
import com.example.model.data.BestMentor
import com.example.model.data.Comment
import com.example.model.data.Rate
import com.example.model.requests.CommentBody
import com.example.model.requests.RateBody
import com.example.res.StringRes
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlin.math.abs

class RateService {

    suspend fun addComment(comment: CommentBody): Resources<Void?> = newSuspendedTransaction {
        val mentor = try {
            UserDao[comment.mentorId]
        } catch (e: EntityNotFoundException) {
            return@newSuspendedTransaction Resources.Error(StringRes.somethingWentWrong)
        }
        val user = try {
            UserDao[comment.userId]
        } catch (e: EntityNotFoundException) {
            return@newSuspendedTransaction Resources.Error(StringRes.somethingWentWrong)
        }
        CommentDao.new {
            this.comment = comment.comment
            this.user = user
            this.mentor = mentor
            date = comment.date
        }
        Resources.Success(null)
    }

    suspend fun getComments(id: Int): Resources<List<Comment>> = newSuspendedTransaction {
        val comments = try {
            UserDao[id].comments.map { it.toComment() }
        } catch (e: EntityNotFoundException) {
            return@newSuspendedTransaction Resources.Error(StringRes.somethingWentWrong)
        }
        Resources.Success(comments)
    }

    suspend fun rate(body: RateBody): Resources<Rate> = newSuspendedTransaction {
        val rateDao =
            RateDao.find { Rates.mentorId eq body.mentorId and (Rates.userId eq body.userId) }.toList().firstOrNull()
        if (rateDao == null) {
            RateDao.new {
                mentorId = body.mentorId
                userId = body.userId
                rate = body.rate
            }
        } else {
            rateDao.rate = body.rate
        }
        var avg = 0F
        val mentorsRates = RateDao.find { Rates.mentorId eq body.mentorId }.toList()
        for (rate in mentorsRates) {
            avg += rate.rate
        }
        avg /= mentorsRates.size
        val mentor = UserDao.find { Users.id eq body.mentorId }.firstOrNull()
        mentor?.rate = avg
        Resources.Success(Rate(avg))
    }

    suspend fun allRates(): List<Rate> = newSuspendedTransaction {
        RateDao.all().map { Rate(it.rate.toFloat()) }
    }

    suspend fun getBestMentors(): Resources<List<BestMentor>> = newSuspendedTransaction {
        try {
            val subjects = SubjectDao.all().toList()
            val bestMentors = arrayListOf<BestMentor>()
            for (subject in subjects) {
                val mentors = subject.mentors.toList()
                if (mentors.isEmpty()) {
                    continue
                }
                var bestMentor = mentors.first()
                for (mentor in mentors) {
                    if (bestMentor.rate < mentor.rate) {
                        bestMentor = mentor
                    }
                }
                if (bestMentor.rate != -1f) {
                    bestMentors.add(BestMentor(bestMentor.toMentor(), subject.toSubjectWithoutMentors()))
                }
            }
            Resources.Success(bestMentors)
        } catch (e: Exception) {
            Resources.Error(StringRes.somethingWentWrong)
        }
    }

}