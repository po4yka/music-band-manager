package com.herokuapp.musicband.services

import com.herokuapp.musicband.data.Song
import com.herokuapp.musicband.data.SongEntity
import org.jetbrains.exposed.sql.transactions.transaction

class SongService {

    fun getAllSongs(): Iterable<Song> = transaction {
        SongEntity.all().map(SongEntity::toSong)
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
