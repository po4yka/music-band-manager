package com.herokuapp.musicband.data

import java.time.LocalDate
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.date

object Songs : IntIdTable() {
    val name = varchar("name", 255)
    val author = varchar("author", 255)
    val groupId = integer("group_id")
    val creationYear = date("creation_year")
    val composer = varchar("composer", 255)
}

/**
 * Represents row in a table
 */
class SongEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SongEntity>(Songs)

    // use Kotlin delegates to map the values of the row to their
    // corresponding columns of the database table
    var name by Songs.name
    var author by Songs.author
    var groupId by Songs.groupId
    var creationYear by Songs.creationYear
    var composer by Songs.composer

    override fun toString(): String = "Group($name, $author, $groupId, $creationYear, $composer)"

    // transform Entity to a simple Kotlin data class
    fun toSong() = Song(name, author, groupId, creationYear, composer)
}

data class Song(
    var name: String,
    var author: String,
    var groupId: Int,
    var creationYear: LocalDate,
    var composer: String
)

/**
 * Data class representation of Song entity for output after using JOIN for
 * changing group_id to group_name
 */
data class SongOut(
    var name: String,
    var author: String,
    var groupName: String,
    var creationYear: LocalDate,
    var composer: String
)
