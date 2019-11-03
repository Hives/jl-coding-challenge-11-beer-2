import org.http4k.core.HttpHandler
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main() {
    val port = 9999
    val server = server(port, endpoints("http://pubcrawlapi.appspot.com/"))
    println("Starting application on port 9999")
    server.start()
}

fun server(port: Int, endpoints: HttpHandler): Http4kServer = endpoints.asServer(Jetty(port))
