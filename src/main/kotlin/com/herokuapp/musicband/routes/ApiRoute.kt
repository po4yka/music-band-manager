package com.herokuapp.musicband.routes

import io.ktor.routing.Routing
import io.ktor.routing.route

fun Routing.apiRoute() {
    route("/api/v1") {
        default()
        groups()
        performers()
        songs()
    }
}
