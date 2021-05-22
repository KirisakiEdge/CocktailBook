package com.example.cocktailbook.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private lateinit var auth: FirebaseAuth

    suspend fun login(email: String, password: String): Result<Any> {
        auth = Firebase.auth
        return suspendCoroutine {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        it.resume(Result.Success(task.result!!.user))
                    }
                }.addOnFailureListener { ex ->
                        it.resume(Result.Error(ex))
                }
        }
    }

    suspend fun register(email: String, password: String): Result<Any>{
        auth = Firebase.auth
        return suspendCoroutine {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        it.resume(Result.Success(task.result!!.user))
                    }
                }.addOnFailureListener { ex ->
                        it.resume(Result.Error(ex))
                    }
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}