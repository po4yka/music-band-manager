package com.herokuapp.musicband.services

import com.herokuapp.musicband.data.Groups
import com.herokuapp.musicband.data.RepertoireSong
import com.herokuapp.musicband.data.Song
import com.herokuapp.musicband.data.SongEntity
import com.herokuapp.musicband.data.SongOut
import com.herokuapp.musicband.data.Songs
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class SongService {

    fun getAllSongs(): Iterable<Song> = transaction {
        SongEntity.all().map(SongEntity::toSong)
    }

    fun getSongsWithGroups(): Iterable<SongOut> = transaction {
        Songs.join(Groups, JoinType.INNER, additionalConstraint = { Songs.groupId eq Groups.id })
            .slice(Songs.name, Songs.author, Songs.creationYear, Songs.composer, Groups.groupName)
            .selectAll()
            .map { SongOut(
                it[Songs.name],
                it[Songs.author],
                it[Groups.groupName],
                it[Songs.creationYear],
                it[Songs.composer])
            }
    }

    fun getRepertoire(groupName: String): Iterable<RepertoireSong> = transaction {
        Songs.join(Groups, JoinType.INNER, additionalConstraint = { Songs.groupId eq Groups.id })
            .slice(Songs.name, Songs.author)
            .select { Groups.groupName eq groupName }
            .map { RepertoireSong(
                it[Songs.name],
                it[Songs.author])
            }
    }

    fun addSong(song: Song) = transaction {
        SongEntity.new {
            this.name = song.name
            this.author = song.author
            this.groupId = song.groupId
            this.creationYear = song.creationYear
            this.composer = song.composer
        }
    }

    fun deleteSong(songId: Int) = transaction {
        SongEntity[songId].delete()
    }
}
