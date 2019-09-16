import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun beerServer(port: Int): Http4kServer = beerRoutes().asServer(Jetty(port))

fun beerRoutes(): HttpHandler = routes(
    "/" bind GET to { _: Request -> Response(OK).body("Hello") },
    "/beerapi/beers" bind GET to { _: Request -> Response(OK) }
)
