package com.herokuapp.musicband.pages

import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.onClick
import kotlinx.html.script
import kotlinx.html.styleLink
import kotlinx.html.title

fun HTML.index() {
    head {
        title("Music Band")
        styleLink("/static/style.css")
        script(src = "/static/script.js") {}
    }
    body {
        h1 {
            +"Welcome, to the music band manager database"
        }
        div(classes = "topnav") {
            a(classes = "active") {
                href = "#groups"
                onClick = """updateTable("groups");"""
                +"Groups"
            }
            a {
                href = "#performers"
                +"Performers"
            }
            a {
                href = "#songs"
                +"Songs"
            }
            a {
                href = "#tour-programs"
                +"Tours"
            }
            a {
                href = "#concerts"
                +"Concerts"
            }
        }
    }
}
