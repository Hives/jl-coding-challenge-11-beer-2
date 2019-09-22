import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes

fun endpoints(): HttpHandler = routes(
    "/" bind GET to { _: Request -> Response(OK).body("Hello") },
    "/beerapi/beers" bind GET to { _: Request -> Response(OK) }
)
