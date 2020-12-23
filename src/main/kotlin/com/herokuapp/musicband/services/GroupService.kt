package com.herokuapp.musicband.services

import com.herokuapp.musicband.data.Group
import com.herokuapp.musicband.data.GroupEntity
import com.herokuapp.musicband.data.GroupName
import com.herokuapp.musicband.data.GroupPerformer
import com.herokuapp.musicband.data.Groups
import com.herokuapp.musicband.data.Performers
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

class GroupService {

    fun getAllGroups(): Iterable<Group> = transaction {
        GroupEntity.all().orderBy(Groups.hitParadePlace to SortOrder.ASC).map(GroupEntity::toGroup)
    }

    fun addGroup(group: Group) {
        transaction {
            GroupEntity.new {
                this.groupName = group.groupName
                this.creationTime = group.creationTime
                this.country = group.country
                this.hitParadePlace = group.hitParadePlace
            }
        }
    }

    fun getAnniversaryGroups(): Iterable<GroupName>? = transaction {
        TransactionManager.current().exec(
            "SELECT group_name " +
                "FROM groups " +
                "WHERE MOD(CAST(DATE_PART('year', current_date) - DATE_PART('year', creation_time) as bigint), 10) = 0;"
        ) { rc ->
            val result = ArrayList<GroupName>()
            while (rc.next()) {
                result.add(GroupName(rc.getString("group_name")))
            }
            println(result)
            result
        }
    }

    fun getYoungestArtist() = transaction {
        Groups.join(Performers, JoinType.INNER, additionalConstraint = { Performers.groupId eq Groups.id })
            .slice(Performers.fullName, Groups.groupName)
            .select { Performers.role eq "singer" }
            .orderBy(Performers.birthday, SortOrder.DESC)
            .limit(1)
            .map { GroupPerformer(
                it[Groups.groupName],
                it[Performers.fullName]
            ) }
    }

    fun getAvgAgeLower() = transaction {
        TransactionManager.current().exec(
            "SELECT group_name " +
                "FROM ( " +
                "SELECT group_name, date_part('year', age(p.birthday::date)) as avg_age " +
                "FROM groups " +
                "INNER JOIN performers p on groups.id = p.group_id " +
                "GROUP BY groups.group_name, p.birthday " +
                ") AS A " +
                "GROUP BY A.group_name " +
                "HAVING SUM(A.avg_age) / COUNT(group_name) < 45;"
        ) {
            rc ->
            val result = ArrayList<GroupName>()
            while (rc.next()) {
                result.add(GroupName(rc.getString("group_name")))
            }
            println(result)
            result
        }
    }

    fun deleteGroup(groupId: Int) = transaction {
        GroupEntity[groupId].delete()
    }
}
