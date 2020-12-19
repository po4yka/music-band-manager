package com.herokuapp.musicband.data

import java.time.LocalDate
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.date

object Performers : IntIdTable() {
    val fullName = varchar("full_name", 255)
    val birthday = date("birthday")
    val groupId = integer("group_id")
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
    var groupId by Performers.groupId
    var role by Performers.role

    override fun toString(): String = "Performer($fullName, $birthday, $groupId, $role)"

    // transform Entity to a simple Kotlin data class
    fun toPerformer() = Performer(fullName, birthday, groupId, role)
}

data class Performer(
    val fullName: String,
    val birthday: LocalDate,
    val groupId: Int,
    val role: String
)
