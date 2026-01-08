package org.signa.app.domain.util

sealed interface Result<out D, out E: Error> {
    data class Success<out D>(val data: D): Result<D, Nothing>
    data class Error<out E: org.signa.app.domain.util.Error>(val error: E): Result<Nothing, E>
}

interface Error

enum class DataError: Error {
    NO_INTERNET,
    SERVER_ERROR,
    UNKNOWN,
    PERMISSION_DENIED
}
