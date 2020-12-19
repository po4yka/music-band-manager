package com.herokuapp.musicband.data

import java.time.LocalDate
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.date

object TourPrograms : IntIdTable() {
    val name = varchar("name", 255)
    val groupId = integer("group_id")
    val startDate = date("start_date")
    val endDate = date("end_date")
}

/**
 * Represents row in a table
 */
class TourProgramEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TourProgramEntity>(TourPrograms)

    // use Kotlin delegates to map the values of the row to their
    // corresponding columns of the database table
    var name by TourPrograms.name
    var groupId by TourPrograms.groupId
    var startDate by TourPrograms.startDate
    var endDate by TourPrograms.endDate

    override fun toString(): String = "TourProgram($name, $groupId, $startDate, $endDate)"

    // transform Entity to a simple Kotlin data class
    fun toTourProgram() = TourProgram(name, groupId, startDate, endDate)
}

data class TourProgram(
    val name: String,
    val groupId: Int,
    val startDate: LocalDate,
    val endDate: LocalDate
)

data class TourProgramOut(
    val name: String,
    val groupName: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)
