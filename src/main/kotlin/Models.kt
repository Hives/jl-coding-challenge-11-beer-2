import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
data class Pub(
    val name: String,
    val regularBeers: List<String>,
    val guestBeers: List<String>,
    val pubService: String,
    val id: Int,
    val createTS: String
)

@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
data class Pubs(val pubs: List<Pub>)

data class Beer(
    val name: String,
    val pub: String,
    val pubService: String,
    val isRegular: Boolean
)
