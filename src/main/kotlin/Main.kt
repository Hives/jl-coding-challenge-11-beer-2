import org.http4k.core.HttpHandler
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main() {
    val server = server(9999, endpoints("https://pubcrawlapi.appspot.com/"))
    server.start()
}

fun server(port: Int, endpoints: HttpHandler): Http4kServer = endpoints.asServer(Jetty(port))
