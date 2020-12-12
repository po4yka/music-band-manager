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
            this.group = performer.group
            this.role = performer.role
        }
    }

    fun deletePerformer(groupId: Int) = transaction {
        PerformerEntity[groupId].delete()
    }
}
