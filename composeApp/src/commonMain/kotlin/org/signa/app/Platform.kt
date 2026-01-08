package org.signa.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
