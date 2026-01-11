package org.signa.app.data.source

import android.content.Context
import org.koin.java.KoinJavaComponent.getKoin

actual fun getPlatformScanner(): SignalScanner {
    val context = getKoin().get<Context>()
    return AndroidSignalScanner(context)
}