package com.herokuapp.musicband.data

import java.time.Instant
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.timestamp

object Concerts : IntIdTable() {
    val tourProgramId = integer("tour_program_id")
    val dateTime = timestamp("date_time")
    val place = varchar("place", 255)
    val ticketsCount = integer("tickets_count")
    val hallRentalCost = integer("hall_rental_cost")
    val ticketCost = integer("ticket_cost")
}

/**
 * Represents row in a table
 */
class ConcertEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ConcertEntity>(Concerts)

    // use Kotlin delegates to map the values of the row to their
    // corresponding columns of the database table
    var tourProgramId by Concerts.tourProgramId
    var dateTime by Concerts.dateTime
    var place by Concerts.place
    var ticketsCount by Concerts.ticketsCount
    var hallRentalCost by Concerts.hallRentalCost
    var ticketCost by Concerts.ticketCost

    override fun toString(): String = "Concert($tourProgramId, $dateTime, $place, $ticketsCount, $hallRentalCost, $ticketCost)"

    // transform Entity to a simple Kotlin data class
    fun toConcert() = Concert(tourProgramId, dateTime, place, ticketsCount, hallRentalCost, ticketCost)
}

data class Concert(
    var tourProgramId: Int,
    val dateTime: Instant,
    var place: String,
    var ticketsCount: Int,
    var hallRentalCost: Int,
    var ticketCost: Int
)

data class ConcertOut(
    var tourProgramName: String,
    var groupName: String,
    val dateTime: Instant,
    var place: String,
    var ticketsCount: Int,
    var hallRentalCost: Int,
    var ticketCost: Int
)

data class TicketCost(
    var ticketCost: Int,
    var place: String
)

data class TourConcerts(
    var place: String,
    var dateTime: Instant
)
