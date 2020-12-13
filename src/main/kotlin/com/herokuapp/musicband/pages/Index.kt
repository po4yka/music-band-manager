package com.herokuapp.musicband.pages

import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.script
import kotlinx.html.styleLink
import kotlinx.html.title

fun HTML.index() {
    head {
        title("Music Band")
        styleLink("resources/css/style.css")
        script(src = "resources/js/script.js") {}
    }
    body {
        h1 {
            +"Welcome, to the music band manager database"
        }
        div(classes = "topnav") {
            a(classes = "active") {
                href = "#home"
                +"Home"
            }
            a {
                href = "#news"
                +"News"
            }
            a {
                href = "#contact"
                +"Contact"
            }
        }
    }
}
