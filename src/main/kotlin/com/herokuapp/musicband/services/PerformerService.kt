package com.herokuapp.musicband.services

import com.herokuapp.musicband.data.Groups
import com.herokuapp.musicband.data.Lineup
import com.herokuapp.musicband.data.Performer
import com.herokuapp.musicband.data.PerformerEntity
import com.herokuapp.musicband.data.PerformerOut
import com.herokuapp.musicband.data.Performers
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PerformerService {

    fun getAllPerformers(): Iterable<Performer> = transaction {
        PerformerEntity.all().map(PerformerEntity::toPerformer)
    }

    fun getPerformersWithGroups(): Iterable<PerformerOut> = transaction {
        Performers.join(Groups, JoinType.INNER, additionalConstraint = { Performers.groupId eq Groups.id })
            .slice(Performers.fullName, Performers.birthday, Performers.role, Groups.groupName)
            .selectAll()
            .map { PerformerOut(
                it[Performers.fullName],
                it[Performers.birthday],
                it[Performers.role],
                it[Groups.groupName])
            }
    }

    fun getLineup(groupName: String): Iterable<Lineup> = transaction {
        Performers.join(Groups, JoinType.INNER, additionalConstraint = { Performers.groupId eq Groups.id })
            .slice(Performers.fullName, Performers.role, Performers.birthday)
            .select { Groups.groupName eq groupName }
            .map { Lineup(
                it[Performers.fullName],
                it[Performers.role],
                it[Performers.birthday])
            }
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
