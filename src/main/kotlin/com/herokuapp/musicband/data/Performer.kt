package com.herokuapp.musicband.data

import java.time.LocalDate
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.date

object Performers : IntIdTable() {
    val fullName = varchar("group_name", 255)
    val birthday = date("creation_time")
    val group = varchar("country", 255)
    val role = varchar("country", 255)
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
    var group by Performers.group
    var role by Performers.role

    override fun toString(): String = "Performer($fullName, $birthday, $group, $role)"

    // transform Entity to a simple Kotlin data class
    fun toPerformer() = Performer(fullName, birthday, group, role)
}

data class Performer(
    val fullName: String,
    val birthday: LocalDate,
    val group: String,
    val role: String
)
