package kz.kakainchik.fourthwalllpicsum.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A http model response for getting a picture
 */
@Serializable
data class PictureResponse(val id: Long,
                           val author: String,
                           val width: Int,
                           val height: Int,
                           val url: String,
                           @SerialName("download_url")
                           val downloadUrl: String)