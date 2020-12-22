package com.herokuapp.musicband.routes

import com.google.gson.Gson
import com.herokuapp.musicband.data.GroupName
import com.herokuapp.musicband.data.RepertoireSong
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

    post("repertoire") {
        println("POST /repertoire")
        val json = call.receive<String>()
        println(json)
        if (json.isEmpty()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val repertoireRequest = Gson().fromJson(json, RepertoireSong::class.java)
        println(repertoireRequest)
        val repertoire = songService.getRepertoire(repertoireRequest.name)
        println("Repertoire: $repertoire")
        call.respond(repertoire)
    }

    post("lasttourrep") {
        println("POST /lasttourrep")
        val json = call.receive<String>()
        println(json)
        if (json.isEmpty()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val lastTourRepRequest = Gson().fromJson(json, GroupName::class.java)
        println(lastTourRepRequest)
        val lastTourRepertoire = songService.getLastTourRepertoire(lastTourRepRequest.name)
        println("Last tour repertoire: $lastTourRepertoire")
        call.respond(lastTourRepertoire)
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
