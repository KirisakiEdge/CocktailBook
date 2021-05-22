package com.example.cocktailbook.ui.user

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.*
import com.example.cocktailbook.R
import com.example.cocktailbook.data.LoginDataSource
import com.example.cocktailbook.data.LoginRepository
import com.example.cocktailbook.data.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableStateFlow<LoginFormState?>(null)
    val loginFormState: StateFlow<LoginFormState?> = _loginForm

    private val _loginResult =  MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult

    suspend fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            Log.e("ViewModelSuc", result.toString())
            _loginResult.emit(LoginResult(success = LoggedInUserView(displayName = result.data.email!!)))
        }
        if(result is Result.Error){
            Log.e("ViewModelFail", result.toString())
            _loginResult.emit(LoginResult(error = (result.toString())))
        }

    }
    suspend fun register(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.register(username, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.email!!))
        } else {
            _loginResult.value = LoginResult(error = result.toString())
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
class UserViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource())) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}