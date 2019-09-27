import org.http4k.core.*
import org.http4k.lens.Query
import org.http4k.lens.double
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind

private val floatLens = { name: String -> Query.double().required(name) }

internal fun findBeersEndpoint(findBeers: BeerFinder): RoutingHttpHandler {
    return "/beers" bind Method.GET to { request: Request ->
        val lng = floatLens("lng")(request)
        val lat = floatLens("lat")(request)
        val deg = floatLens("deg")(request)

        val location = Location(lng, lat, deg)
        val beers = findBeers(location)
        Response(Status.OK).with(Beers.format of Beers(beers))
    }
}
