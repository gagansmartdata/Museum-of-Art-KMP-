package com.jetbrains.kmpapp.di

import com.jetbrains.kmpapp.data.InMemoryMuseumStorage
import com.jetbrains.kmpapp.data.MuseumStorage
import com.jetbrains.kmpapp.data.repository.MuseumRepositoryImpl
import com.jetbrains.kmpapp.domain.repository.MuseumRepository
import com.jetbrains.kmpapp.domain.usecase.GetMuseumById
import com.jetbrains.kmpapp.domain.usecase.GetMuseumList
import com.jetbrains.kmpapp.screens.detail.DetailScreenModel
import com.jetbrains.kmpapp.screens.list.ListScreenModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    single {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(ContentNegotiation) {
                // TODO Fix API so it serves application/json
                json(json, contentType = ContentType.Any)
            }
        }
    }

    single<MuseumRepository> { MuseumRepositoryImpl(get()) }
    single<MuseumStorage> { InMemoryMuseumStorage() }
    single {
        GetMuseumById(get())
        GetMuseumList(get())
    }
}

val screenModelsModule = module {
    factoryOf(::ListScreenModel)
    factory {
        DetailScreenModel(GetMuseumById(get()))
    }
}

fun initKoin() {
    startKoin {
        modules(
            dataModule,
            screenModelsModule,
        )
    }
}
