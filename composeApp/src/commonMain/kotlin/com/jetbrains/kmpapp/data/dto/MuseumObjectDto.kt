package com.jetbrains.kmpapp.data.dto

import com.jetbrains.kmpapp.domain.model.MuseumObject
import kotlinx.serialization.Serializable

@Serializable
data class MuseumObjectDto(
    val objectID: Int,
    val title: String,
    val artistDisplayName: String,
    val medium: String,
    val dimensions: String,
    val objectURL: String,
    val objectDate: String,
    val primaryImage: String,
    val primaryImageSmall: String,
    val repository: String,
    val department: String,
    val creditLine: String,
){
    fun toMuseumObject() = MuseumObject(objectID, title, artistDisplayName, medium, dimensions, objectURL, objectDate, primaryImage, primaryImageSmall, repository, department, creditLine)
}
