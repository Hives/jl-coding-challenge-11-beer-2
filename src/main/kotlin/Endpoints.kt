import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.routing.bind
import org.http4k.routing.routes

internal fun endpoints(pubCrawlUri: String): HttpHandler {
    val pubApiClient = createApiClient(Uri.of(pubCrawlUri))
    val findPubs = createFindPubs(pubApiClient)
    val findBeers = createFindBeers(findPubs)

    return ServerFilters.CatchLensFailure
        .then(
            routes(
                "/" bind GET to { Response(OK).body("Hello 2") },
                findBeersEndpoint(findBeers)
            )
        )
}
