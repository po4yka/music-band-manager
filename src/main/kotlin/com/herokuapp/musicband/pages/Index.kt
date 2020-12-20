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
import kotlinx.html.unsafe

fun HTML.index() {
    head {
        title("Music Band")
        styleLink("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css")
        styleLink("/static/style.css")
        script(src = "https://code.jquery.com/jquery-3.3.1.slim.min.js") { }
        script(src = "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js") { }
        script(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js") { }
        script(src = "/static/index.js") { defer = true }
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
                onClick = """updateTable("tour-programs");"""
                +"Tours"
            }
            a {
                id = "concertsNav"
                href = "#concerts"
                onClick = """updateTable("concerts");"""
                +"Concerts"
            }
        }
        unsafe {
            +"""
                <button type="button" id="addBtnMain" class="btn btn-primary btn-lg btn-block" data-toggle="modal" data-target="#addModal" onclick="addNewElementMain()">Add</button>
            """.trimIndent()
        }
        table(classes = "table-fill") {
            id = "dataTable"
        }
        unsafe {
            +"""
                <div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="addModalTitleLabel" aria-hidden="true">
                  <div class="modal-dialog" role="document">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h5 class="modal-title" id="addModalTitleLabel">Modal title</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                          <span aria-hidden="true">&times;</span>
                        </button>
                      </div>
                      <div id="addModalBody" class="modal-body">
                        <div id="addNewElMainDropdown" class="dropdown">
                            <p>TEST MESSAGE</p>
                            <button class="btn btn-primary dropdown-toggle" id="addNewElMainDropMenu" type="button" data-toggle="dropdown">Dropdown Example
                            <span class="caret"></span></button>
                            <ul id="addElDropdownList" class="dropdown-menu" role="menu" aria-labelledby="addNewElMainDropMenu">
                                <li role="presentation"><a role="menuitem" tabindex="-1" href="#">HTML</a></li>
                                <li role="presentation"><a role="menuitem" tabindex="-1" href="#">CSS</a></li>
                                <li role="presentation"><a role="menuitem" tabindex="-1" href="#">JavaScript</a></li>
                            </ul>
                        </div>
                      </div>
                      <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary">Add</button>
                      </div>
                    </div>
                  </div>
                </div>
            """.trimIndent()
        }
    }
}
