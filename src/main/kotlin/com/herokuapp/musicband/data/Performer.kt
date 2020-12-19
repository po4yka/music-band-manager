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

// object PerformersOut : IntIdTable() {
//     val fullName = varchar("full_name", 255)
//     val birthday = date("birthday")
//     val role = varchar("role", 255)
//     val groupName = varchar("group_name", 255)
// }

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
    fun toPerformer() = Performer(fullName, birthday, role, groupId)

    fun toPerformerOut(groupName: String) = PerformerOut(fullName, birthday, role, groupName)
}

// class PerformerOutEntity(id: EntityID<Int>) : IntEntity(id) {
//     companion object : IntEntityClass<PerformerEntity>(Performers)
//
//     var fullName by PerformersOut.fullName
//     var birthday by PerformersOut.birthday
//     var role by PerformersOut.role
//     var groupName by PerformersOut.groupName
//
//     override fun toString(): String = "Performer($fullName, $birthday, $groupName, $role)"
//
//     fun toPerformerOut() = PerformerOut(fullName, birthday, role, groupName)
// }

/**
 * With inherit from base abstract class we will get error
 * of 'multiple JSON fields named'
 */

/**
 * Data class representation of Performer entity from DB
 */
data class Performer(
    val fullName: String,
    val birthday: LocalDate,
    val role: String,
    val groupId: Int
)

/**
 * Data class representation of Performer entity for output after using JOIN for
 * changing group_id to group_name
 */
data class PerformerOut(
    val fullName: String,
    val birthday: LocalDate,
    val role: String,
    val groupName: String
)
