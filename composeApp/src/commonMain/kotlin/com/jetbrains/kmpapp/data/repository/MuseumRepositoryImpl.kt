package com.jetbrains.kmpapp.data.repository

import com.jetbrains.kmpapp.data.dto.MuseumObjectDto
import com.jetbrains.kmpapp.domain.model.MuseumObject
import com.jetbrains.kmpapp.domain.repository.MuseumRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlin.coroutines.cancellation.CancellationException

class MuseumRepositoryImpl(private val client: HttpClient) : MuseumRepository {
    companion object {
        private const val API_URL =
            "https://raw.githubusercontent.com/Kotlin/KMP-App-Template/main/list.json"
    }

    override suspend fun getData(): List<MuseumObjectDto> {
        return try {
            client.get(API_URL).body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()

            emptyList()
        }
    }
}