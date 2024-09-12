package com.jetbrains.kmpapp.domain.repository

import com.jetbrains.kmpapp.data.dto.MuseumObjectDto

interface MuseumRepository {
    suspend fun getData(): List<MuseumObjectDto>
}