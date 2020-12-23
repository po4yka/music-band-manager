package com.herokuapp.musicband.routes

import com.google.gson.Gson
import com.herokuapp.musicband.data.GroupName
import com.herokuapp.musicband.data.PerformerChangeGroup
import com.herokuapp.musicband.data.PerformerOut
import com.herokuapp.musicband.services.PerformerService
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

fun Route.performers() {
    val performerService by di().instance<PerformerService>()

    get("performer") {
        val allPerformers = performerService.getPerformersWithGroups()
        println("GET all performers")
        call.respond(allPerformers)
    }

    post("lineup") {
        println("POST /lineup")
        val json = call.receive<String>()
        println(json)
        if (json.isEmpty()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val lineupRequest = Gson().fromJson(json, GroupName::class.java)
        println(lineupRequest)
        val lineup = performerService.getLineup(lineupRequest.name)
        println("Lineup: $lineup")
        call.respond(lineup)
    }

    post("performer") {
        val performerRequest = call.receive<PerformerOut>()
        performerService.addPerformer(performerRequest)
        call.respond(HttpStatusCode.Accepted)
    }

    post("chggroup") {
        println("POST /chggroup")
        val json = call.receive<String>()
        println(json)
        if (json.isEmpty()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val performerRequest = Gson().fromJson(json, PerformerChangeGroup::class.java)
        println(performerRequest)
        performerService.chgGroup(performerRequest)
        call.respond(HttpStatusCode.Accepted)
    }

    delete("performer/{id}") {
        val performerId = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
        performerService.deletePerformer(performerId)
        call.respond(HttpStatusCode.OK)
    }
}
