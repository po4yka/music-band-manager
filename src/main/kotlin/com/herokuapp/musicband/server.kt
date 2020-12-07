import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.html.respondHtml
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.html.*

fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor. Test message."
        }
    }
}

fun main() {
    val port = Integer.valueOf(System.getenv("PORT"))
    embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            get("hello") {
                call.respond(HttpStatusCode.Accepted, "Hello")
            }
        }
    }.start(wait = true)
}
