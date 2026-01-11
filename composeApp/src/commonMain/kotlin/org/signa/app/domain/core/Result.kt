package org.signa.app.domain.core

sealed interface Result<out D, out E: Error> {
    data class Success<out D>(val data: D): Result<D, Nothing>
    data class Error<out E: org.signa.app.domain.core.Error>(val error: E): Result<Nothing, E>
}

inline fun <D, E: Error, R> Result<D, E>.map(transform: (D) -> R): Result<R, E> {
    return when(this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(transform(data))
    }
}