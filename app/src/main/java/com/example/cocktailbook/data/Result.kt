package com.example.cocktailbook.data

import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseUser

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: FirebaseUser) : Result<T>()
    data class Error<out T : Any>(val exception: Throwable) : Result<T>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error<*> -> exception.message!!
        }
    }
}