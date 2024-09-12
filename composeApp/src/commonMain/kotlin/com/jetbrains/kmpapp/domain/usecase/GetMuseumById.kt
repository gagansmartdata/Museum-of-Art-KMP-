package com.jetbrains.kmpapp.domain.usecase

import com.jetbrains.kmpapp.domain.model.MuseumObject
import com.jetbrains.kmpapp.domain.repository.MuseumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMuseumById(private val museumRepository: MuseumRepository) {

    operator fun invoke(museumId: Int): Flow<MuseumObject> {
        return flow {
            museumRepository.getData().find { it.objectID == museumId }?.toMuseumObject()
                ?.let { emit(it) }
        }
    }
}