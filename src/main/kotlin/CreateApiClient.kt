import org.http4k.client.OkHttp
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters

internal fun createApiClient(apiUri: Uri) = ClientFilters.SetBaseUriFrom(apiUri).then(OkHttp())
