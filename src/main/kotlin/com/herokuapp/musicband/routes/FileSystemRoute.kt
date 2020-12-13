package com.herokuapp.musicband.routes

import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.routing.Route
import java.io.File

fun Route.routeFilesystem() {
    static("static") {
        staticRootFolder = File("resources/frontend")
        files("css")
        files("js")
    }
}
