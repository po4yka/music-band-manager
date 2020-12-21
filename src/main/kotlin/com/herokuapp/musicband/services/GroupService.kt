package com.herokuapp.musicband.services

import com.herokuapp.musicband.data.Group
import com.herokuapp.musicband.data.GroupEntity
import java.sql.SQLException
import org.jetbrains.exposed.sql.transactions.transaction

class GroupService {

    fun getAllGroups(): Iterable<Group> = transaction {
        GroupEntity.all().map(GroupEntity::toGroup)
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

    fun deleteGroup(groupId: Int) = transaction {
        GroupEntity[groupId].delete()
    }
}
