package org.signa.app.domain.core

enum class DataError: Error {
    // Network Errors
    NO_INTERNET,
    SERVER_ERROR,
    SERIALIZATION,
    REQUEST_TIMEOUT,
    TOO_MANY_REQUESTS,

    // Database Errors
    LOCAL_DB_ERROR,
    NOT_FOUND,

    UNKNOWN
}