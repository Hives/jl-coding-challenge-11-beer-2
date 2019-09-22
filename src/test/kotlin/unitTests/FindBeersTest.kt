package unitTests

import Beer
import Location
import Pub
import FindPubs
import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import createFindBeers
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object FindBeersTest : Spek({
    describe("finding beers") {
        val pub1 = Pub(
            name = "Example pub 1",
            regularBeers = listOf(
                "Regular beer"
            ),
            guestBeers = listOf(),
            pubService = "http://example.com/pub1",
            id = 1,
            createTS = "pub 1 created timestamp"
        )

        val pub2 = Pub(
            name = "Example pub 2",
            regularBeers = listOf(),
            guestBeers = listOf(
                "Guest beer"
            ),
            pubService = "http://example.com/pub2",
            id = 2,
            createTS = "pub 2 created timestamp"
        )

        val mockLocation = mockk<Location>()
        val mockFindPubs = mockk<FindPubs>()

        val findPubsCalledWith = slot<Location>()
        every { mockFindPubs(capture(findPubsCalledWith)) }
            .returns(listOf(pub1, pub2))

        val findBeers = createFindBeers(mockFindPubs)
        val actualOutput = findBeers(mockLocation)

        it("calls pub finder with the right location") {
            assertThat(findPubsCalledWith.captured).isEqualTo(mockLocation)
        }

        it("the correct two beers are returned") {
            val expectedBeer1 = Beer(
                name = "Regular beer",
                pub = "Example pub 1",
                pubService = "http://example.com/pub1",
                regular = true
            )
            val expectedBeer2 = Beer(
                name = "Guest beer",
                pub = "Example pub 2",
                pubService = "http://example.com/pub2",
                regular = false
            )
            assertThat(actualOutput).containsOnly(expectedBeer1, expectedBeer2)
        }
    }

    describe("when there are duplicate entries") {
        val oldEntry1 = Pub(
            name = "Example pub",
            regularBeers = listOf(
                "Old regular beer"
            ),
            guestBeers = listOf(),
            pubService = "http://example.com/pub1",
            id = 1,
            createTS = "2019-01-01 10:30:00"
        )

        val oldEntry2 = Pub(
            name = "Example pub",
            regularBeers = listOf(),
            guestBeers = listOf(
                "Old guest beer"
            ),
            pubService = "http://example.com/pub1",
            id = 1,
            createTS = "2019-01-02 10:30:00"
        )

        val newEntry = Pub(
            name = "Example pub",
            regularBeers = listOf(
                "New regular beer"
            ),
            guestBeers = listOf(
                "New guest beer"
            ),
            pubService = "http://example.com/pub1",
            id = 1,
            createTS = "2019-01-03 10:30:00"
        )

        val mockLocation = mockk<Location>()
        val mockFindPubs = mockk<FindPubs>()
        val findPubsCalledWith = slot<Location>()
        every { mockFindPubs(capture(findPubsCalledWith)) }
            .returns(listOf(oldEntry1, newEntry, oldEntry2))

        val findBeers = createFindBeers(mockFindPubs)
        val actualOutput = findBeers(mockLocation)

        it("beers from old entries are not extracted") {
            assertThat(actualOutput.filter { it.name == "Old regular beer" }).isEmpty()
            assertThat(actualOutput.filter { it.name == "Old guest beer" }).isEmpty()
        }

        it("beers from newest entry are extracted") {
            assertThat(actualOutput.filter { it.name == "New regular beer" }).isNotEmpty()
            assertThat(actualOutput.filter { it.name == "New guest beer" }).isNotEmpty()
        }
    }
})
