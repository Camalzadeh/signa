package org.signa.app.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.koin.dsl.module
import org.signa.app.BuildConfig
import org.signa.app.data.local.AppDatabase
import org.signa.app.data.local.getDatabaseBuilder
import org.signa.app.data.repository.AiRepositoryImpl
import org.signa.app.data.repository.SignalRepositoryImpl
import org.signa.app.data.service.AiService
import org.signa.app.data.service.GeminiAiService
import org.signa.app.data.source.getPlatformScanner
import org.signa.app.domain.repository.AiRepository
import org.signa.app.domain.repository.SignalRepository

private const val API_KEY = "AIzaSyBDyI6k6tHsvlAiZptwh4OY7OyJvxzNTaY"

val dataModule = module {
    single {
        getDatabaseBuilder()
            .fallbackToDestructiveMigration(true)
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<AppDatabase>().signalDao() }
    single { get<AppDatabase>().aiDao() }

    single { getPlatformScanner() }
//    single<AiService> { GeminiAiService(apiKey = BuildConfig.GEMINI_API_KEY) }
    single<AiService> { GeminiAiService(apiKey = API_KEY) }

    single<SignalRepository> { SignalRepositoryImpl(get(), get()) }
    single<AiRepository> { AiRepositoryImpl(get(), get()) }
}