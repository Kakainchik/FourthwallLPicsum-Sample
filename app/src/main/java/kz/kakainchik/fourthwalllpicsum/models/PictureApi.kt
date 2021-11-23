package kz.kakainchik.fourthwalllpicsum.models


import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kz.kakainchik.fourthwalllpicsum.entities.Picture
import kz.kakainchik.fourthwalllpicsum.entities.PictureResponse

/**
 * The service that allows to sent requests to `Lorem Picsum` API.
 */
class PictureApi(private val client: HttpClient) {
    /**
     * Received list of pictures' data.
     */
    suspend fun makeGetPicturesListRequest(page: Int): List<Picture> =
        withContext(Dispatchers.Default) {
            val response: HttpResponse = client.get(PICTURES_LIST_URL) {
                header(HttpHeaders.Accept, ContentType.Application.Json)

                parameter("page", page)
            }

            when(response.status) {
                HttpStatusCode.OK -> {
                    val body = Json.decodeFromString<Array<PictureResponse>>(response.readText())
                    body.map { Picture(it.id, it.downloadUrl, it.author) }
                }
                else -> emptyList<Picture>()
            }
        }
}