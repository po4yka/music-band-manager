package com.herokuapp.musicband.services

import com.herokuapp.musicband.data.TourProgram
import com.herokuapp.musicband.data.TourProgramEntity
import org.jetbrains.exposed.sql.transactions.transaction

class TourProgramService {

    fun getAllTourPrograms(): Iterable<TourProgram> = transaction {
        TourProgramEntity.all().map(TourProgramEntity::toTourProgram)
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
