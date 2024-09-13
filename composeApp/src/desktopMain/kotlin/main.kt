package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.jetbrains.kmpapp.App
import com.jetbrains.kmpapp.di.dataModule
import com.jetbrains.kmpapp.di.initKoin
import com.jetbrains.kmpapp.di.screenModelsModule
import org.koin.core.context.startKoin

fun main() =
    application {
        initKoin()
        Window(
            onCloseRequest = ::exitApplication,
            title = "KotlinProject",
        ) {
            App()
        }
}