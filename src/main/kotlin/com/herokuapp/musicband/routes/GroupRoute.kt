package com.herokuapp.musicband.routes

import com.herokuapp.musicband.data.Group
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
        val bookRequest = call.receive<String>()
        // groupService.addGroup(bookRequest)
        println(bookRequest)
        call.respond(HttpStatusCode.Accepted)
    }

    delete("group/{id}") {
        val bookId = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
        groupService.deleteGroup(bookId)
        call.respond(HttpStatusCode.OK)
    }
}
