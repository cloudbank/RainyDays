package com.droidteahouse.rainydays.ui.login

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.droidteahouse.rainydays.R
import com.droidteahouse.rainydays.login.LoginFormState
import com.droidteahouse.rainydays.ui.login.LoginRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    ViewModel() {


    val loginFormState = MutableLiveData<LoginFormState>()


    val loginResult: MutableLiveData<Result> = MutableLiveData()


    fun isLoggedIn(): Boolean {
        return loginRepository.isLoggedIn
    }

    fun login(username: String, password: String) {
        loginRepository.login(username, password, loginResult)
    }

    fun register(username: String, password: String) {
        loginRepository.register(username, password, loginResult)
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            loginFormState.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            loginFormState.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            loginFormState.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches() && username.isNotBlank()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun logout() {
        loginRepository.logout()
    }
}
