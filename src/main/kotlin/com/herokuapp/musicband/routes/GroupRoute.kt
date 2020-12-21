package com.herokuapp.musicband.routes

import com.google.gson.Gson
import com.herokuapp.musicband.data.Group
import com.herokuapp.musicband.removeQuotesAndUnescape
import com.herokuapp.musicband.services.GroupService
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

fun Route.groups() {

    val groupService by di().instance<GroupService>()

    get("group") {
        val allBooks = groupService.getAllGroups()
        println("GET all groups")
        call.respond(allBooks)
    }

    post("group") {
        println("POST /group")
        val json = call.receive<String>()
        println(removeQuotesAndUnescape(json))
        val bookRequest = Gson().fromJson(json, Group::class.java)
        println(bookRequest)
        try {
            groupService.addGroup(bookRequest)
        } catch (exp: java.sql.SQLException) {
            call.respond(HttpStatusCode.InternalServerError, "Incorrect key for new Group")
            return@post
        }
        call.respond(HttpStatusCode.Accepted, "Group was successfully added")
    }

    delete("group/{id}") {
        val bookId = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
        groupService.deleteGroup(bookId)
        call.respond(HttpStatusCode.OK)
    }
}
