package com.jetbrains.kmpapp.screens.list

import cafe.adriel.voyager.core.model.ScreenModel
import com.jetbrains.kmpapp.domain.model.MuseumObject
import com.jetbrains.kmpapp.domain.usecase.GetMuseumList
import kotlinx.coroutines.flow.Flow

class ListScreenModel(museumRepository: GetMuseumList) : ScreenModel {
    val objects: Flow<List<MuseumObject>> =  museumRepository()
//        museumRepository().stateIn(CoroutineScope(Dispatchers.Main + SupervisorJob() ), SharingStarted.WhileSubscribed(5000), emptyList())

}
