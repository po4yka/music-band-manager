package com.herokuapp.musicband.services

import com.herokuapp.musicband.data.Performer
import com.herokuapp.musicband.data.PerformerEntity
import org.jetbrains.exposed.sql.transactions.transaction

class PerformerService {

    fun getAllPerformers(): Iterable<Performer> = transaction {
        PerformerEntity.all().map(PerformerEntity::toPerformer)
    }

    fun addPerformer(performer: Performer) = transaction {
        PerformerEntity.new {
            this.fullName = performer.fullName
            this.birthday = performer.birthday
            this.groupId = performer.groupId
            this.role = performer.role
        }
    }

    fun deletePerformer(performerId: Int) = transaction {
        PerformerEntity[performerId].delete()
    }
}
