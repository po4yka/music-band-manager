package com.herokuapp.musicband.routes

import com.herokuapp.musicband.pages.index
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.html.HTML

fun Route.default() {
    get("/") {
        call.respondHtml(HttpStatusCode.OK, HTML::index)
    }
}
