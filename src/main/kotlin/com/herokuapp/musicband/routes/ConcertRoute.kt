package com.herokuapp.musicband.routes

import com.google.gson.Gson
import com.herokuapp.musicband.data.Concert
import com.herokuapp.musicband.data.GroupName
import com.herokuapp.musicband.services.ConcertService
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

fun Route.concerts() {
    val concertService by di().instance<ConcertService>()

    get("concert") {
        val allConcerts = concertService.getAllOutConcerts()
        println("GET all concerts")
        call.respond(allConcerts)
    }

    post("lastconcertcost") {
        println("POST /lastconcertcost")
        val json = call.receive<String>()
        println(json)
        if (json.isEmpty()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val lastConcertCostRequest = Gson().fromJson(json, GroupName::class.java)
        println(lastConcertCostRequest)
        val ticketCost = concertService.getLastConcertTicketCost(lastConcertCostRequest.name)
        call.respond(ticketCost)
    }

    post("lasttourplacedate") {
        println("POST /lasttourplacedate")
        val json = call.receive<String>()
        println(json)
        if (json.isEmpty()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val lastTourPlacesDatesRequest = Gson().fromJson(json, GroupName::class.java)
        println(lastTourPlacesDatesRequest)
        val lastTourPlacesDate = concertService.getLastTourPlaceDate(lastTourPlacesDatesRequest.name)
        println(lastTourPlacesDate)
        call.respond(lastTourPlacesDate)
    }

    post("concert") {
        val concertRequest = call.receive<Concert>()
        concertService.addConcert(concertRequest)
        call.respond(HttpStatusCode.Accepted)
    }

    delete("concert/{id}") {
        val concertId = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
        concertService.deleteConcert(concertId)
        call.respond(HttpStatusCode.OK)
    }
}
