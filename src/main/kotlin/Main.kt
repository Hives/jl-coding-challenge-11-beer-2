import org.http4k.core.HttpHandler
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main() {
    val server = server(9999, endpoints("http://localhost:3002/"))
    server.start()
}

fun server(port: Int, endpoints: HttpHandler): Http4kServer = endpoints.asServer(Jetty(port))
