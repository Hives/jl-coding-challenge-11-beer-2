import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind

internal fun findBeersEndpoint(findBeers: BeerFinder): RoutingHttpHandler {
    return "/beers" bind Method.GET to { request: Request ->
        val lng = request.query("lng")
        val lat = request.query("lat")
        val deg = request.query("deg")
        if (lng != null && lat != null && deg != null) {
            val location = Location(lng.toDouble(), lat.toDouble(), deg.toDouble())
            val beers = findBeers(location)
            Response(Status.OK).with(Beers.format of Beers(beers))
        } else {
            Response(Status.OK)
        }
    }
}
