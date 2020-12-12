package com.herokuapp.musicband.services

import com.herokuapp.musicband.data.Concert
import com.herokuapp.musicband.data.ConcertEntity
import org.jetbrains.exposed.sql.transactions.transaction

class ConcertService {

    fun getAllConcerts(): Iterable<Concert> = transaction {
        ConcertEntity.all().map(ConcertEntity::toConcert)
    }

    fun addConcert(concert: Concert) = transaction {
        ConcertEntity.new {
            this.tourProgramId = concert.tourProgramId
            this.dateTime = concert.dateTime
            this.place = concert.place
            this.ticketsCount = concert.ticketsCount
            this.hallRentalCost = concert.hallRentalCost
            this.ticketCost = concert.ticketCost
        }
    }

    fun deleteConcert(concertId: Int) = transaction {
        ConcertEntity[concertId].delete()
    }
}
