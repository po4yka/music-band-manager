package com.herokuapp.musicband.routes

import com.herokuapp.musicband.data.Song
import com.herokuapp.musicband.services.SongService
import io.ktor.application.call
import io.ktor.features.NotFoundException
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import org.kodein.di.instance
import org.kodein.di.ktor.di

fun Route.songs() {
    val songService by di().instance<SongService>()

    get("song") {
        val allSongs = songService.getSongsWithGroups()
        println("GET all songs")
        call.respond(allSongs)
    }

    post("song") {
        val songRequest = call.receive<Song>()
        songService.addSong(songRequest)
        call.respond(HttpStatusCode.Accepted)
    }

    delete("song/{id}") {
        val songId = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
        songService.deleteSong(songId)
        call.respond(HttpStatusCode.OK)
    }
}
