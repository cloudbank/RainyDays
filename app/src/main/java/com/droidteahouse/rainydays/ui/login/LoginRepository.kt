package com.droidteahouse.rainydays.ui.login

import androidx.lifecycle.MutableLiveData
import com.droidteahouse.rainydays.ui.login.LoginDataSource

import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository @Inject constructor(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean =
        dataSource.isLoggedIn() != null

    fun logout() {
        user = null
        dataSource.logout()
    }
    fun login(username: String, password: String,liveData: MutableLiveData<Result>) {
         dataSource.login(username, password, liveData)
      }

    fun register(username: String, password: String, liveData: MutableLiveData<Result>) {
               dataSource.register(username, password, liveData)
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }


}
