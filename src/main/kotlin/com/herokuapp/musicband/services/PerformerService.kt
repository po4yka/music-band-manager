package com.herokuapp.musicband.services

import com.herokuapp.musicband.data.Groups
import com.herokuapp.musicband.data.Lineup
import com.herokuapp.musicband.data.Performer
import com.herokuapp.musicband.data.PerformerChangeGroup
import com.herokuapp.musicband.data.PerformerEntity
import com.herokuapp.musicband.data.PerformerOut
import com.herokuapp.musicband.data.Performers
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

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

    fun chgGroup(performer: PerformerChangeGroup) = transaction {
        val newGroupId = Groups.select { Groups.groupName eq performer.newGroupName }.single()[Groups.id]
        Performers.update({ (Performers.fullName eq performer.fullName) and (Performers.birthday eq performer.birthday) }) {
            it[Performers.groupId] = newGroupId.value
        }
    }

    fun addPerformer(performer: PerformerOut) = transaction {
        val groupId = Groups.select { Groups.groupName eq performer.groupName }.single()[Groups.id]
        PerformerEntity.new {
            this.fullName = performer.fullName
            this.birthday = performer.birthday
            this.groupId = groupId.value
            this.role = performer.role
        }
    }

    fun deletePerformer(performerId: Int) = transaction {
        PerformerEntity[performerId].delete()
    }
}
