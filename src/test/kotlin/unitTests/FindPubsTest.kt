package unitTests

import Beer
import Location
import Pub
import Pubs
import assertk.assertThat
import assertk.assertions.isEqualTo
import createFindPubs
import jsonMapper
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object FindPubsTest : Spek({
    describe("the pub finder") {

        class MockPubCrawlApi(fakeResponseJson: String) {
            lateinit var receivedRequest: Request

            val mock =
                "/pubcache" bind GET to { request ->
                    this.receivedRequest = request
                    Response(OK)
                        .header("Content-type", "application/json")
                        .body(fakeResponseJson)
                }
        }

        describe("calls the API with the correct parameters") {
            val mockApiResponse = """{
                |   "Pubs": [{
                |       "Name": "",
                |       "RegularBeers": [],
                |       "GuestBeers": [],
                |       "PubService": "",
                |       "Id": "",
                |       "CreateTS": ""
                |   }]
                |}""".trimMargin()

            val mockPubCrawlApi = MockPubCrawlApi(mockApiResponse)
            val pubFinder = createFindPubs(mockPubCrawlApi.mock)

            val location = Location(
                lat = 20.00,
                lng = 40.00,
                deg = 0.003
            )

            pubFinder(location)

            it("calls the correct url") {
                assertThat(mockPubCrawlApi.receivedRequest.uri.path).isEqualTo("/pubcache/")
            }

            it("the api is called with the specified longitude") {
                assertThat(mockPubCrawlApi.receivedRequest.query("lng")).isEqualTo(location.lng.toString())
            }

            it("the api is called with the specified latitude") {
                assertThat(mockPubCrawlApi.receivedRequest.query("lat")).isEqualTo(location.lat.toString())
            }

            it("the api is called with the specified deg") {
                assertThat(mockPubCrawlApi.receivedRequest.query("deg")).isEqualTo(location.deg.toString())
            }
        }

        describe("returns the API response") {
            val expectedPub = Pub(
                name = "Example pub",
                regularBeers = listOf(
                    "Regular beer 1",
                    "Regular beer 2"
                ),
                guestBeers = listOf(
                    "Guest beer 1",
                    "Guest beer 2"
                ),
                pubService = "http://example.com/pub/123",
                id = 12345,
                createTS = "2019-01-02 10:30:00"
            )
            val mockApiResponse = jsonMapper().writeValueAsString(Pubs(listOf(expectedPub)))

            val mockPubCrawlApi = MockPubCrawlApi(mockApiResponse)
            val pubFinder = createFindPubs(mockPubCrawlApi.mock)
            val pubs = pubFinder(Location(1.0, 1.0, 1.0))

            val returnedPub = pubs.single()

            it("returns the expected pub") {
                assertThat(returnedPub).isEqualTo(expectedPub)
            }

        }

        describe("it doesn't fall over if regular and guest beers are not provided") {
            val dummyJson = """
                |{
                |   "Pubs": [{
                |       "Name": "Example pub",
                |       "PubService": "http://example.com/pub/123",
                |       "Id": "12345",
                |       "CreateTS": "2019-01-02 10:30:00"
                |   }]
                |}""".trimMargin()

            val mockPubCrawlApi = MockPubCrawlApi(dummyJson)
            val pubFinder = createFindPubs(mockPubCrawlApi.mock)
            val response = pubFinder(Location(1.0, 1.0, 1.0))

            it("regular and guest beer default to empty lists") {
                val pub = response.single()
                assertThat(pub.regularBeers).isEqualTo(emptyList<Beer>())
                assertThat(pub.guestBeers).isEqualTo(emptyList<Beer>())
            }

        } 
    }
})
