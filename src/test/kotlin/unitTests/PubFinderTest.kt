package unitTests

import Pub
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import createPubFinder
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
            |       "Name": "",
            |       "RegularBeers": [],
            |       "GuestBeers": [],
            |       "PubService": "",
            |       "Id": "12345",
            |       "CreateTS": "2019-01-01 10:30:00",
            |       "IrrelevantProperty": "We don't care about this"
            |   }, {
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
            |       "CreateTS": "2019-01-03 10:30:00",
            |       "IrrelevantProperty": "We don't care about this"
            |   }, {
            |       "Name": "",
            |       "RegularBeers": [],
            |       "GuestBeers": [],
            |       "PubService": "",
            |       "Id": "12345",
            |       "CreateTS": "2019-01-02 10:30:00",
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

        describe("gives the correct response") {
            it("returns only the most recent of duplicate pubs") {
                assertThat(pubs).hasSize(1)
                assertThat(pubs.single().createTS).isEqualTo("2019-01-03 10:30:00")
            }

            it("the returned object contains a pub with the right name") {
                assertThat(pubs.single().name).isEqualTo("Example pub")
            }

            it("the returned object contains a pub with the right regular beers") {
                assertThat(pubs.single().regularBeers).isEqualTo(listOf("Regular beer 1", "Regular beer 2"))
            }

            it("the returned object contains a pub with the right guest beers") {
                assertThat(pubs.single().guestBeers).isEqualTo(listOf("Guest beer 1", "Guest beer 2"))
            }

            it("the returned object contains a pub with the right pubservice") {
                assertThat(pubs.single().pubService).isEqualTo("http://example.com/pub/abc123")
            }

            it("the returned object contains a pub with the right id") {
                assertThat(pubs.single().id).isEqualTo(12345)
            }
        }
    }
})
