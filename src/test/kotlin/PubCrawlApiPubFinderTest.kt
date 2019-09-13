import assertk.assertThat
import assertk.assertions.isEqualTo
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object PubCrawlApiPubFinderTest: Spek({
    describe("PubCrawlApiPubFinder") {

        val mockPubCrawlApi = MockPubCrawlApi()

        val pubCrawlApiPubFinder = createPubCrawlApiPubFinder(mockPubCrawlApi.mock)

        val lat = 20.00
        val lng = 40.00
        val range = 0.003

        pubCrawlApiPubFinder(lat, lng, range)

        it("calls the correct url") {
            assertThat(mockPubCrawlApi.request.uri.path).isEqualTo("/pubcache")
        }

        it("api is called with the specified latitude") {
            assertThat(mockPubCrawlApi.request.query("lat")).isEqualTo(lat.toString())
        }

        it("api is called with the specified longitude") {
            assertThat(mockPubCrawlApi.request.query("lng")).isEqualTo(lng.toString())
        }

        it("api is called with the specified range") {
            assertThat(mockPubCrawlApi.request.query("deg")).isEqualTo(range.toString())
        }
    }
})

private class MockPubCrawlApi {
    lateinit var request: Request

    val mock =
        "/pubcache" bind GET to { request ->
            this.request = request
            Response(OK)
        }
}
