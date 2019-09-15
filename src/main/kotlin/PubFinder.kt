import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.format.Jackson.auto

private val pubsLens = Body.auto<Pubs>().toLens()

internal fun createPubFinder(client: HttpHandler) =
    fun(lat: Double, lng: Double, range: Double): List<Pub> {
        val response = client(Request(GET, "/pubcache?lat=${lat}&lng=${lng}&deg=${range}"))
        val pubs = pubsLens(response)
        return pubs.pubs
    }
