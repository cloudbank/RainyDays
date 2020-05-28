package com.droidteahouse.rainydays.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.droidteahouse.rainydays.ui.login.Status
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
@Singleton
class LoginDataSource  @Inject constructor(){
    private var mAuth: FirebaseAuth


    var numCores = Runtime.getRuntime().availableProcessors()
    var executor: ThreadPoolExecutor = ThreadPoolExecutor(
        numCores * 2, numCores * 2,
        60L, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>()
    )

    companion object {
        val TAG = LoginDataSource::class.java.canonicalName
    }

    init {
        mAuth = FirebaseAuth.getInstance()
    }

    fun isLoggedIn(): FirebaseUser? {
        return mAuth?.getCurrentUser()
    }

    fun login(email: String, password: String, liveData: MutableLiveData<Result>) {

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                executor,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = mAuth.currentUser
                        val result = Result()
                        result.data = user as FirebaseUser
                        result.status = Status.SUCCESS
                        liveData.postValue(result)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        val result = Result()
                        result.msg = ("Error logging in")
                        result.status = Status.FAILED
                        liveData.postValue(result)
                    }
                })

    }

    fun register(email: String, password: String,liveData: MutableLiveData<Result>) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                executor,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createAccount:success")
                        val user = mAuth.currentUser
                        val result = Result()
                        result.data = user
                        result.status = Status.SUCCESS
                        liveData.postValue(result)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createAccount:failure", task.exception)
                        val result = Result()
                        result.msg = ("Error logging in")
                        result.status = Status.FAILED
                        liveData.postValue(result)
                    }

                })

    }

    fun logout() {

    }


}

