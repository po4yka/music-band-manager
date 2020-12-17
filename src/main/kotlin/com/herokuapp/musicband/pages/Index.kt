package com.herokuapp.musicband.pages

import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.onClick
import kotlinx.html.script
import kotlinx.html.styleLink
import kotlinx.html.table
import kotlinx.html.title

fun HTML.index() {
    head {
        title("Music Band")
        styleLink("/static/style.css")
        script(src = "/static/script.js") {defer}
    }
    body {
        h1 {
            +"Welcome, to the music band manager database"
        }
        div(classes = "topnav") {
            a(classes = "active") {
                id = "groupsNav"
                href = "#groups"
                onClick = """updateTable("groups");"""
                +"Groups"
            }
            a {
                id = "performersNav"
                href = "#performers"
                onClick = """updateTable("performers");"""
                +"Performers"
            }
            a {
                id = "songsNav"
                href = "#songs"
                onClick = """updateTable("songs");"""
                +"Songs"
            }
            a {
                id = "tour-programsNav"
                href = "#tour-programs"
                onClick = """updateTable("tour-programs);"""
                +"Tours"
            }
            a {
                id = "concertsNav"
                href = "#concerts"
                onClick = """updateTable("concerts);"""
                +"Concerts"
            }
        }
        table(classes = "table-fill") {
            id = "dataTable"
        }
    }
}
