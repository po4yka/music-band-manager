package com.herokuapp.musicband.services

import com.herokuapp.musicband.data.Groups
import com.herokuapp.musicband.data.TourProgram
import com.herokuapp.musicband.data.TourProgramEntity
import com.herokuapp.musicband.data.TourProgramOut
import com.herokuapp.musicband.data.TourPrograms
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.selectAll
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
