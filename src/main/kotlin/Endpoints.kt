import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes

internal fun endpoints(): HttpHandler = routes(
    "/" bind GET to { Response(OK).body("Hello") },
    findBeersEndpoint()
)
