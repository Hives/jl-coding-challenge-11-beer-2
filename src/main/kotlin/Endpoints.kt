import org.http4k.core.*
import org.http4k.routing.bind
import org.http4k.routing.routes

fun endpoints(): HttpHandler = routes(
    "/" bind Method.GET to { _: Request -> Response(Status.OK).body("Hello") },
    "/beerapi/beers" bind Method.GET to { _: Request -> Response(Status.OK) }
)
