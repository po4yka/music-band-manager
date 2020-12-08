package com.herokuapp.musicband.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.date
import java.time.LocalDate

object Groups : IntIdTable() {
    val groupName = varchar("group_name", 255)
    val creationTime = date("creation_time")
    val country = varchar("country", 255)
    val hitParadePlace = integer("hit_parade_place")
}

/**
 * Represents row in a table
 */
class GroupEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GroupEntity>(Groups)

    // use Kotlin delegates to map the values of the row to their
    // corresponding columns of the database table
    var groupName by Groups.groupName
    var creationTime by Groups.creationTime
    var country by Groups.country
    var hitParadePlace by Groups.hitParadePlace

    override fun toString(): String = "Group($groupName, $creationTime, $country, $hitParadePlace)"

    // transform Entity to a simple Kotlin data class
    fun toGroup() = Group(groupName, creationTime, country, hitParadePlace)
}

data class Group(
    val groupName: String,
    val creationTime: LocalDate,
    val country: String,
    val hitParadePlace: Int
);