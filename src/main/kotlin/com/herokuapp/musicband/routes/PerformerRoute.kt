package com.herokuapp.musicband.routes

import com.herokuapp.musicband.data.Performer
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