import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.startsWith
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object FindBeersFunctionalTest : Spek ({
    val location = Location(-0.141499, 51.496466, 0.003)

    val fakePubCrawlApi = routes(
        "/pubcache/" bind Method.GET to { request ->
            val lng = request.query("lng")
            val lat = request.query("lat")
            val deg = request.query("deg")
            if (lng != null && lat != null && deg != null) {
                val requestedLocation = Location(lng.toDouble(), lat.toDouble(), deg.toDouble())
                if (requestedLocation != location) {
                    Response(BAD_REQUEST)
                } else {
                    Response(OK)
                        .body(fakePubCrawlResponse)
                        .header("Content-Type", "application/json")
                }
            } else {
                Response(BAD_REQUEST)
            }
        }
    )

    val pubCrawlApi = fakePubCrawlApi.asServer(Jetty(7777))

    val application by lazy { endpoints("http://localhost:7777/") }

    beforeGroup { pubCrawlApi.start() }

    describe("/beers should get beers") {
        val request = Request(Method.GET, "/beers?lat=${location.lat}&lng=${location.lng}&deg=${location.deg}")
        val response by lazy { application(request) }

        val returnedBeers by lazy {
            jsonMapper().readValue(response.bodyString(), Beers::class.java).beers
        }

        it("returns success") {
            assertThat(response.status).isEqualTo(OK)
        }

        it("content type is application/json") {
            val contentTypeHeader = response.header("Content-Type") ?: "wtf"
            assertThat(contentTypeHeader).startsWith(ContentType.APPLICATION_JSON.value)
            // better to make own matcher here to deal with possible null?
        }

        it("the response contains the expected beers") {
            assertThat(returnedBeers).contains(expectedBeer1)
        }

    }

    afterGroup { pubCrawlApi.stop() }
})

private val fakePubCrawlResponse = """{
    "Pubs": [{
      "Name": "Wetherspoons",
      "Address": "Unit 5, Upper Concourse, Victoria Station, Terminus Place Victoria London SW1V 1JT",
      "Town": "London",
      "PostCode": "SW1V 1JT",
      "PhotoURL": "https://whatpub.com/img/WLD/15953/wetherspoons-london/200/150",
      "Telephone": "02079310445",
      "OpeningTimes": "",
      "MealTimes": "",
      "Owner": "Wetherspoon",
      "About": "Just named Wetherspoons, this site was named by Tim Martin, chairman of JDW, after his primary school teacher in New Zealand! The pub overlooks the station concourse on both sides and has recently been refurbished in a bright cafÃ© bar design. The interior features blue and cream tiling, two curved bars with marble style tops and the welcome addition of banquettes along the opposite side. The large hexagonal lampshades and snugs introduce design characteristics reminiscent of the 1920s and 1930s. The twelve handpumps on two bar counters dispense regular beers and changing guest ales from smaller brewers such as Thornbridge and often local brewers like Portobello; the alcohol licence allows retail sale from 8am. Screens show times of train departures. Note that British Transport police sometimes close the bar when football fans are due through the station.",
      "GuestBeerDesc": "This pub serves 9 changing beers. ",
      "Lng": -0.144311,
      "Lat": 51.495241,
      "RegularBeers": [
        "Fuller#039;s London Pride",
        "Greene King IPA"
      ],
      "GuestBeers": [
        "Adnams --varies--",
        "Windsor and Eton --varies--"
      ],
      "Features": [
        "Real Ale Available",
        "Real Cider Available",
        "CAMRA Voucher Scheme",
        "Cask Marque Accredited",
        "Quiet Pub"
      ],
      "Facilities": [
        "Disabled Access - Lift to upper concourse is not well signed.",
        "Lunchtime Meals",
        "Evening Meals",
        "Family Friendly - To 9pm.",
        "Wifi"
      ],
      "PubService": "https://pubcrawlapi.appspot.com/pub/?v=1&id=15953&branch=WLD&uId=&pubs=yes&realAle=yes&memberDiscount=no&town=London",
      "Id": "15953",
      "Branch": "WLD",
      "CreateTS": "2019-08-03 12:47:18",
      "Message": {
        "Status": 0,
        "Text": "Pub retrieved."
      }
    }]
}"""

val expectedBeer1 = Beer(
    name = "Fuller#039;s London Pride",
    pub = "Wetherspoons",
    pubService = "https://pubcrawlapi.appspot.com/pub/?v=1&id=15953&branch=WLD&uId=&pubs=yes&realAle=yes&memberDiscount=no&town=London",
    regular = true
)

val jsonMapper = {
    jacksonObjectMapper()
}
