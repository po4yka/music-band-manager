package com.herokuapp.musicband.routes

import com.herokuapp.musicband.services.RepertoireService
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import org.kodein.di.instance
import org.kodein.di.ktor.di

fun Route.repertoires() {

    val repertoireService by di().instance<RepertoireService>()

    get("repertoire") {
        val allRepertoires = repertoireService.getAllRepertoires()
        println("GET all repertoires")
        call.respond(allRepertoires)
    }
}
