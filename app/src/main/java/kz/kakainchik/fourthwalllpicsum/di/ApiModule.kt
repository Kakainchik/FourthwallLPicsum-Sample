package kz.kakainchik.fourthwalllpicsum.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kz.kakainchik.fourthwalllpicsum.models.CONNECTION
import kz.kakainchik.fourthwalllpicsum.models.KTOR_TAG
import kz.kakainchik.fourthwalllpicsum.models.SHORT_TIMEOUT
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {
    /**
     * The default client with default parameters.
     */
    @Singleton
    @Provides
    fun provideApiClient(): HttpClient {
        return HttpClient(OkHttp) {
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
    }
}