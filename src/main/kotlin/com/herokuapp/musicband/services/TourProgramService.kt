package com.herokuapp.musicband.services

import com.herokuapp.musicband.data.Concerts
import com.herokuapp.musicband.data.Groups
import com.herokuapp.musicband.data.TourProgram
import com.herokuapp.musicband.data.TourProgramEntity
import com.herokuapp.musicband.data.TourProgramInfo
import com.herokuapp.musicband.data.TourProgramOut
import com.herokuapp.musicband.data.TourPrograms
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.avg
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.sum
import org.jetbrains.exposed.sql.transactions.transaction

class TourProgramService {

    fun getAllTourPrograms(): Iterable<TourProgram> = transaction {
        TourProgramEntity.all().map(TourProgramEntity::toTourProgram)
    }

    fun getAllTourProgramsOut(): Iterable<TourProgramOut> = transaction {
        TourPrograms.join(Groups, JoinType.INNER, additionalConstraint = { TourPrograms.groupId eq Groups.id })
            .slice(TourPrograms.name, TourPrograms.startDate, TourPrograms.endDate, Groups.groupName)
            .selectAll()
            .map { TourProgramOut(
                it[TourPrograms.name],
                it[Groups.groupName],
                it[TourPrograms.startDate],
                it[TourPrograms.endDate])
            }
    }

    fun getLastTourInfo(groupName: String): Iterable<TourProgramInfo> = transaction {
        val revenue = Expression.build { Concerts.ticketsCount * Concerts.ticketCost - Concerts.hallRentalCost }
        TourPrograms.join(Concerts, JoinType.INNER, additionalConstraint = { Concerts.tourProgramId eq TourPrograms.id })
            .join(Groups, JoinType.INNER, additionalConstraint = { Groups.id eq TourPrograms.groupId })
            .slice(TourPrograms.name, TourPrograms.startDate, TourPrograms.endDate, revenue, Concerts.ticketsCount.sum(), Concerts.ticketCost.avg())
            .select { Groups.groupName eq groupName }
            .groupBy(TourPrograms.id, TourPrograms.endDate)
            .orderBy(TourPrograms.endDate, SortOrder.DESC)
            .map { TourProgramInfo(
                it[TourPrograms.name],
                it[TourPrograms.startDate],
                it[TourPrograms.endDate],
                it[revenue],
                it[Concerts.ticketsCount.sum()] ?: 0,
                it[Concerts.ticketCost.avg()]!!.toDouble())
            }
    }

    fun addTourProgram(tourProgramId: TourProgram) = transaction {
        TourProgramEntity.new {
            this.name = tourProgramId.name
            this.groupId = tourProgramId.groupId
            this.startDate = tourProgramId.startDate
            this.endDate = tourProgramId.endDate
        }
    }

    fun deleteTourProgram(tourProgramId: Int) = transaction {
        TourProgramEntity[tourProgramId].delete()
    }
}
