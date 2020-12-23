package com.herokuapp.musicband.services

import com.herokuapp.musicband.data.Group
import com.herokuapp.musicband.data.GroupEntity
import com.herokuapp.musicband.data.GroupName
import com.herokuapp.musicband.data.Groups
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import sun.util.calendar.CalendarUtils.mod

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

    fun getAnniversaryGroups(): Iterable<GroupName>? {
        return TransactionManager.current().exec(
            "SELECT group_name" +
                "FROM groups" +
                "WHERE MOD(CAST(DATE_PART('year', current_date) - DATE_PART('year', creation_time) as bigint), 10) = 0;"
        ) { rc ->
            val result = ArrayList<GroupName>()
            while (rc.next()) {
                result.add(GroupName(rc.getString("groups.group_name")))
            }
            println(result)
            result
        }
    }

    fun deleteGroup(groupId: Int) = transaction {
        GroupEntity[groupId].delete()
    }
}
