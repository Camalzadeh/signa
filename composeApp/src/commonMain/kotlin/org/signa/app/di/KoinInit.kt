package org.signa.app.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration.invoke(this)
        modules(
            dataModule,
            domainModule,
            uiModule
        )
    }
}