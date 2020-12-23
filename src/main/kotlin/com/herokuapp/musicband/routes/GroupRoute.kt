package com.herokuapp.musicband.routes

import com.google.gson.Gson
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
        println("GET all groups")
        val allBooks = groupService.getAllGroups()
        call.respond(allBooks)
    }

    get("anniversary") {
        println("GET anniversary groups")
        val anniversaryGroups = groupService.getAnniversaryGroups()
        call.respond(anniversaryGroups!!)
    }

    post("group") {
        println("POST /group")
        val json = call.receive<String>()
        println(json)
        val bookRequest = Gson().fromJson(json, Group::class.java)
        println(bookRequest)
        groupService.addGroup(bookRequest)
        call.respond(HttpStatusCode.Accepted, "Group was successfully added")
    }

    delete("group/{id}") {
        val bookId = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
        groupService.deleteGroup(bookId)
        call.respond(HttpStatusCode.OK)
    }
}
