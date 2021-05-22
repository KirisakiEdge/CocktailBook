package com.example.cocktailbook.ui.user

import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cocktailbook.R

import com.example.cocktailbook.data.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SessionManager(requireContext()).fetchUserStatus()){
            try {
                findNavController().navigate(R.id.action_loginFragment_to_navigation_user)
            }catch (e: IllegalArgumentException){ }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this, UserViewModelFactory()).get(UserViewModel::class.java)
        val usernameEditText = view.findViewById<EditText>(R.id.username)
        val passwordEditText = view.findViewById<EditText>(R.id.password)
        val registerButton = view.findViewById<Button>(R.id.register)
        val loginButton = view.findViewById<Button>(R.id.login)
        val loadingProgressBar = view.findViewById<ProgressBar>(R.id.loading)


        lifecycleScope.launch(Dispatchers.Main) {
            userViewModel.loginFormState.collect { loginFormState ->
                if (loginFormState != null) {
                    loginButton.isEnabled = loginFormState.isDataValid
                    loginFormState.usernameError?.let {
                        usernameEditText.error = getString(it)
                    }
                    loginFormState.passwordError?.let {
                        passwordEditText.error = getString(it)
                    }
                }
            }
        }


        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                userViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)


        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            lifecycleScope.launch(Dispatchers.Main){
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    userViewModel.login(
                        usernameEditText.text.toString(),
                        passwordEditText.text.toString()
                    )
                }
            }
            false
        }

        loginButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                loadingProgressBar.visibility = View.VISIBLE
                userViewModel.login(usernameEditText.text.toString(), passwordEditText.text.toString())
                userViewModel.loginResult.collect { loginResult ->
                    Log.e("logisssssssssn", "${loginResult.toString()}+++++++")
                    if (loginResult  != null) {
                        loadingProgressBar.visibility = View.GONE
                        loginResult.error?.let {
                            Log.e("Login", it)
                            showLoginFailed(it)

                        }
                        loginResult.success?.let {
                            Log.e("Login", it.toString())
                            updateUiWithUser(it)
                        }
                    }
                }
            }
        }
        registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience
        SessionManager(requireContext()).saveUserStatus(true)
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
        findNavController().navigateUp()
    }

    private fun showLoginFailed(errorString: String) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
}