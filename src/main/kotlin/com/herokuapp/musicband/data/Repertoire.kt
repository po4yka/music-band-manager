package com.herokuapp.musicband.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Repertoires : IntIdTable() {
    val tourProgramId = integer("tour_program_id")
    val songId = integer("song_id")
}

/**
 * Represents row in a table
 */
class RepertoireEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RepertoireEntity>(Repertoires)

    // use Kotlin delegates to map the values of the row to their
    // corresponding columns of the database table
    var tourProgramId by Repertoires.tourProgramId
    var songId by Repertoires.songId

    override fun toString(): String = "Repertoire($tourProgramId, $songId)"

    // transform Entity to a simple Kotlin data class
    fun toRepertoire() = Repertoire(tourProgramId, songId)
}

data class Repertoire(
    val tourProgramId: Int,
    val songId: Int
)
