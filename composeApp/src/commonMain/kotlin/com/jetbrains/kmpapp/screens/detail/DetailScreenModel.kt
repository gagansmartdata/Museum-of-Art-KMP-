package com.jetbrains.kmpapp.screens.detail

import cafe.adriel.voyager.core.model.ScreenModel
import com.jetbrains.kmpapp.domain.model.MuseumObject
import com.jetbrains.kmpapp.domain.usecase.GetMuseumById
import kotlinx.coroutines.flow.Flow

class DetailScreenModel(private val getDetails: GetMuseumById) : ScreenModel {
    fun getObject(objectId: Int): Flow<MuseumObject?> = getDetails(objectId)
}
