package com.jetbrains.kmpapp.screens.list

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.jetbrains.kmpapp.domain.model.MuseumObject
import com.jetbrains.kmpapp.domain.usecase.GetMuseumList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ListScreenModel(museumRepository: GetMuseumList) : ScreenModel {
    val objects: StateFlow<List<MuseumObject>> =
        museumRepository().stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
