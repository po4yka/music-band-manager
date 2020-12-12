package com.herokuapp.musicband.routes

import com.herokuapp.musicband.data.Group
import com.herokuapp.musicband.data.Performer
import com.herokuapp.musicband.data.Song
import com.herokuapp.musicband.services.GroupService
import com.herokuapp.musicband.services.PerformerService
import com.herokuapp.musicband.services.SongService
import io.ktor.application.call
import io.ktor.features.NotFoundException
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.title
import org.kodein.di.instance
import org.kodein.di.ktor.di

fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor. Test message."
        }
    }
}

fun Route.default() {
    get("/") {
        call.respondHtml(HttpStatusCode.OK, HTML::index)
    }
}

fun Route.groups() {

    val groupService by di().instance<GroupService>()

    get("group") {
        val allBooks = groupService.getAllGroups()
        println("GET all groups")
        call.respond(allBooks)
    }

    post("group") {
        val bookRequest = call.receive<Group>()
        groupService.addGroup(bookRequest)
        call.respond(HttpStatusCode.Accepted)
    }

    delete("group/{id}") {
        val bookId = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
        groupService.deleteGroup(bookId)
        call.respond(HttpStatusCode.OK)
    }
}

fun Route.performers() {
    val performerService by di().instance<PerformerService>()

    get("performer") {
        val allPerformers = performerService.getAllPerformers()
        println("GET all performers")
        call.respond(allPerformers)
    }

    post("performer") {
        val performerRequest = call.receive<Performer>()
        performerService.addPerformer(performerRequest)
        call.respond(HttpStatusCode.Accepted)
    }

    delete("performer/{id}") {
        val performerId = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
        performerService.deletePerformer(performerId)
        call.respond(HttpStatusCode.OK)
    }
}

fun Route.songs() {
    val songService by di().instance<SongService>()

    get("song") {
        val allSongs = songService.getAllSongs()
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
