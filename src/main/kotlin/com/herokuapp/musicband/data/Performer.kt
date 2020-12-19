package com.herokuapp.musicband.data

import java.time.LocalDate
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.date
import org.jetbrains.exposed.sql.select

object Performers : IntIdTable() {
    val fullName = varchar("full_name", 255)
    val birthday = date("birthday")
    val residentialGroupId = optReference("group_id", Groups)
    val role = varchar("role", 255)
}

/**
 * Represents row in a table
 */
class PerformerEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PerformerEntity>(Performers)

    // use Kotlin delegates to map the values of the row to their
    // corresponding columns of the database table
    var fullName by Performers.fullName
    var birthday by Performers.birthday
    var residentialGroupId by Performers.residentialGroupId
    var role by Performers.role

    override fun toString(): String = "Performer($fullName, $birthday, $residentialGroupId, $role)"

    // transform Entity to a simple Kotlin data class
    fun toPerformer() {
        val rowId: Int = residentialGroupId?.value ?: -1
        val groupName = Groups.select { Groups.id eq rowId }.
                        withDistinct().map { it[Groups.groupName] }[0]
        Performer(fullName, birthday, groupName, role)
    }
}

data class Performer(
    val fullName: String,
    val birthday: LocalDate,
    val groupName: String,
    val role: String
)
