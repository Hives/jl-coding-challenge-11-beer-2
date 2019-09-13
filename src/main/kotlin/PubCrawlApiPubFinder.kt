import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request

internal fun createPubCrawlApiPubFinder(client: HttpHandler) =
    fun(lat: Double, lng: Double, range: Double) {
        client(Request(GET, "/pubcache?lat=${lat}&lng=${lng}&deg=${range}"))
    }
