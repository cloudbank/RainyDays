package com.droidteahouse.rainydays.ui.login

import com.droidteahouse.rainydays.ui.login.LoggedInUser

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    var success: LoggedInUser? = null,
    var error: String? = null
)

