internal typealias BeerFinder = (Location) -> List<Beer>

internal fun createFindBeers(findPubs: FindPubs) : BeerFinder =
    fun(location: Location) = findPubs(location).extractBeers()

private fun List<Pub>.extractBeers(): List<Beer> = this
    .removeDuplicates()
    .flatMap { it.extractBeers() }

private fun List<Pub>.removeDuplicates(): List<Pub> = this
    .sortedByDescending { it.createTS }
    .distinctBy { it.id }

private fun Pub.extractBeers(): List<Beer> =
    (this.regularBeers.map { it to true } + this.guestBeers.map { it to false })
        .map {
            val (beerName, isRegular) = it
            Beer(
                name = beerName,
                pub = this.name,
                pubService = this.pubService,
                isRegular = isRegular
            )
        }
