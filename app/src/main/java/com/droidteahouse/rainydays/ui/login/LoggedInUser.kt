package com.droidteahouse.rainydays.ui.login

import com.google.firebase.auth.FirebaseUser

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser (
    var fbaseUser : FirebaseUser,
    val userId: String,
    val displayName: String
)
