package com.herokuapp.musicband.routes

import com.google.gson.Gson
import com.herokuapp.musicband.data.GroupName
import com.herokuapp.musicband.data.TourProgram
import com.herokuapp.musicband.services.TourProgramService
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

fun Route.tourPrograms() {
    val tourProgramService by di().instance<TourProgramService>()

    get("tour-program") {
        val allTourPrograms = tourProgramService.getAllTourProgramsOut()
        println("GET all tour programs")
        call.respond(allTourPrograms)
    }

    post("lasttourinfo") {
        println("POST /lasttourinfo")
        val json = call.receive<String>()
        println(json)
        if (json.isEmpty()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val lastTourInfoRequest = Gson().fromJson(json, GroupName::class.java)
        println(lastTourInfoRequest)
        val lastTourInfo = tourProgramService.getLastTourInfo(lastTourInfoRequest.name)
        println("Last tour info: $lastTourInfo")
        call.respond(lastTourInfo)
    }

    post("tour-program") {
        val tourProgramRequest = call.receive<TourProgram>()
        tourProgramService.addTourProgram(tourProgramRequest)
        call.respond(HttpStatusCode.Accepted)
    }

    delete("tour-program/{id}") {
        val tourProgramId = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
        tourProgramService.deleteTourProgram(tourProgramId)
        call.respond(HttpStatusCode.OK)
    }
}
