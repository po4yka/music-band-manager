package com.herokuapp.musicband.services

import com.herokuapp.musicband.data.Concert
import com.herokuapp.musicband.data.ConcertEntity
import com.herokuapp.musicband.data.ConcertOut
import com.herokuapp.musicband.data.Concerts
import com.herokuapp.musicband.data.Groups
import com.herokuapp.musicband.data.TicketCost
import com.herokuapp.musicband.data.TourConcerts
import com.herokuapp.musicband.data.TourPrograms
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ConcertService {

    fun getAllConcerts(): Iterable<Concert> = transaction {
        ConcertEntity.all().map(ConcertEntity::toConcert)
    }

    fun getAllOutConcerts(): Iterable<ConcertOut> = transaction {
        Concerts.join(TourPrograms, JoinType.INNER, additionalConstraint = { Concerts.tourProgramId eq TourPrograms.id })
            .join(Groups, JoinType.INNER, additionalConstraint = { TourPrograms.groupId eq Groups.id })
            .slice(Concerts.dateTime, Concerts.ticketsCount, Concerts.hallRentalCost, Concerts.ticketCost, Concerts.place, TourPrograms.name, Groups.groupName)
            .selectAll()
            .map { ConcertOut(
                    it[TourPrograms.name],
                    it[Groups.groupName],
                    it[Concerts.dateTime],
                    it[Concerts.place],
                    it[Concerts.ticketsCount],
                    it[Concerts.hallRentalCost],
                    it[Concerts.ticketCost]
                )
            }
    }

    fun getLastConcertTicketCost(groupName: String): Iterable<TicketCost> = transaction {
        Concerts.join(TourPrograms, JoinType.INNER, additionalConstraint = { Concerts.tourProgramId eq TourPrograms.id })
            .join(Groups, JoinType.INNER, additionalConstraint = { TourPrograms.groupId eq Groups.id })
            .slice(Concerts.ticketCost, Concerts.place)
            .select { Groups.groupName eq groupName }
            .orderBy(Concerts.dateTime, SortOrder.DESC)
            .limit(1)
            .map { TicketCost(
                it[Concerts.ticketCost],
                it[Concerts.place]) }
    }

    fun getLastTourPlaceDate(groupName: String): Iterable<TourConcerts> = transaction {
        val sequelTable = TourPrograms.alias("sql")
        Concerts.join(TourPrograms, JoinType.INNER, additionalConstraint = { TourPrograms.id eq Concerts.tourProgramId })
            .join(Groups, JoinType.INNER, additionalConstraint = { TourPrograms.groupId eq Groups.id })
            .join(sequelTable, JoinType.LEFT, additionalConstraint = { (TourPrograms.id eq sequelTable[TourPrograms.id]) and (TourPrograms.endDate less sequelTable[TourPrograms.endDate]) })
            .select { (sequelTable[TourPrograms.endDate].isNull()) and (Groups.groupName eq groupName) }
            .map { TourConcerts(
                it[Concerts.place],
                it[Concerts.dateTime]
            ) }
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
