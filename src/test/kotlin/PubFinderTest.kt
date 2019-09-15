import assertk.assertThat
import assertk.assertions.isEqualTo
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object PubFinderTest : Spek({
    describe("the pub finder") {

        val fakeApiResponse = """{
            |   "Pubs": [{
            |       "Name": "Example pub",
            |       "RegularBeers": [
            |           "Regular beer 1",
            |           "Regular beer 2"
            |       ],
            |       "GuestBeers": [
            |           "Guest beer 1",
            |           "Guest beer 2"
            |       ],
            |       "PubService": "http://example.com/pub/abc123",
            |       "Id": "12345",
            |       "CreateTS": "2019-01-01 10:30:00",
            |       "IrrelevantProperty": "We don't care about this"
            |   }]
            |}""".trimMargin()

        class MockPubCrawlApi {
            lateinit var receivedRequest: Request

            val mock =
                "/pubcache" bind GET to { request ->
                    this.receivedRequest = request
                    Response(OK)
                        .header("Content-type", "application/json")
                        .body(fakeApiResponse)
                }
        }

        val mockPubCrawlApi = MockPubCrawlApi()

        val pubFinder = createPubFinder(mockPubCrawlApi.mock)

        val lat = 20.00
        val lng = 40.00
        val range = 0.003

        val pubs = pubFinder(lat, lng, range)

        describe("calls the API with the correct parameters") {
            it("calls the correct url") {
                assertThat(mockPubCrawlApi.receivedRequest.uri.path).isEqualTo("/pubcache")
            }

            it("the api is called with the specified latitude") {
                assertThat(mockPubCrawlApi.receivedRequest.query("lat")).isEqualTo(lat.toString())
            }

            it("the api is called with the specified longitude") {
                assertThat(mockPubCrawlApi.receivedRequest.query("lng")).isEqualTo(lng.toString())
            }

            it("the api is called with the specified range") {
                assertThat(mockPubCrawlApi.receivedRequest.query("deg")).isEqualTo(range.toString())
            }
        }

        describe("returns the correct response") {
            it("the returned object contains a pub with the right name") {
                assertThat(pubs.first().name).isEqualTo("Example pub")
            }

            it("the returned object contains a pub with the right regular beers") {
                assertThat(pubs.first().regularBeers[0]).isEqualTo("Regular beer 1")
                assertThat(pubs.first().regularBeers[1]).isEqualTo("Regular beer 2")
            }

            it("the returned object contains a pub with the right guest beers") {
                assertThat(pubs.first().guestBeers[0]).isEqualTo("Guest beer 1")
                assertThat(pubs.first().guestBeers[1]).isEqualTo("Guest beer 2")
            }

            it("the returned object contains a pub with the right pubservice") {
                assertThat(pubs.first().pubService).isEqualTo("http://example.com/pub/abc123")
            }

            it("the returned object contains a pub with the right id") {
                assertThat(pubs.first().id).isEqualTo(12345)
            }

            it("the returned object contains a pub with the right created timestamp") {
                assertThat(pubs.first().createTS).isEqualTo("2019-01-01 10:30:00")
            }
        }
    }
})
