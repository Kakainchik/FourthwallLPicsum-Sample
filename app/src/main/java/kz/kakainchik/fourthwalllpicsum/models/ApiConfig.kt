package kz.kakainchik.fourthwalllpicsum.models

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

const val HOSTNAME = "https://picsum.photos"
const val PICTURES_LIST_URL = "$HOSTNAME/v2/list"

const val CONNECTION = "keep-alive"

const val SHORT_TIMEOUT = 5_000L

const val KTOR_TAG = "Ktor"

/**
 * The default client with default parameters.
 */
internal val ktorClient = HttpClient(OkHttp) {
    install(HttpTimeout) {
        connectTimeoutMillis = SHORT_TIMEOUT
        socketTimeoutMillis = SHORT_TIMEOUT
    }
    install(DefaultRequest) {
        header(HttpHeaders.Connection, CONNECTION)
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.v(KTOR_TAG, message)
            }
        }
        level = LogLevel.INFO
    }

    engine {
        config {
            retryOnConnectionFailure(true)
        }
    }
}