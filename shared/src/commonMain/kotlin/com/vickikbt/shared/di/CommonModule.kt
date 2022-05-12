package com.vickikbt.shared.di

import com.vickikbt.shared.data.network.ApiService
import com.vickikbt.shared.data.network.ApiServiceImpl
import com.vickikbt.shared.domain.utils.Constants.BASE_URL
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.core.module.Module
import org.koin.dsl.module

val commonModule = module {

    /*single { Settings() }*/

    /**
     * Creates a http client for Ktor that is provided to the
     * API client via constructor injection
     */
    single {
        HttpClient {
            defaultRequest {
                host = BASE_URL
                url { protocol = URLProtocol.HTTPS }
            }

            install(Logging) {
                level = LogLevel.HEADERS
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.e(tag = "Http Client", message = message)
                    }
                }
            }

            install(JsonFeature) {
                serializer = KotlinxSerializer()
                kotlinx.serialization.json.Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            }
        }
    }
    single<ApiService> { ApiServiceImpl(httpClient = get()) }

    /*single { AccessTokenDao(databaseDriverFactory = get()) }*/
    /*single { DailyGoalDao(databaseDriverFactory = get()) }*/

    /*single<AuthRepository> { AuthRepositoryImpl(apiService = get(), accessTokenDao = get()) }
    single<DateTimeRepository> { DateTimeRepositoryImpl() }
    single<SummariesRepository> { SummariesRepositoryImpl(apiService = get(), dailyGoalDao = get()) }*/
}

expect fun platformModule(): Module
