import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind

internal fun findBeersEndpoint(): RoutingHttpHandler {
    return "/beers" bind Method.GET to { _: Request -> Response(Status.OK) }
}
