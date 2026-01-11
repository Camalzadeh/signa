package org.signa.app.data.source

actual fun getPlatformScanner(): SignalScanner {
    return  JvmSignalScanner()
}