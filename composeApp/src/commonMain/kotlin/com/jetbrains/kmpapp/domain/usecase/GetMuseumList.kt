package com.jetbrains.kmpapp.domain.usecase

import com.jetbrains.kmpapp.domain.model.MuseumObject
import com.jetbrains.kmpapp.domain.repository.MuseumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMuseumList(private val museumRepository: MuseumRepository) {

    operator fun invoke(): Flow<List<MuseumObject>> {
        return flow {
            emit(museumRepository.getData().map { it.toMuseumObject() })
        }
    }
}