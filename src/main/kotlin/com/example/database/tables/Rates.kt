package com.example.database.tables

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Rates : IntIdTable() {
    val rate = integer("rate")
    val userId = integer("userId")
    val mentorId = integer("mentorId")
}

class RateDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RateDao>(Rates)

    var rate by Rates.rate
    var userId by Rates.userId
    var mentorId by Rates.mentorId
}