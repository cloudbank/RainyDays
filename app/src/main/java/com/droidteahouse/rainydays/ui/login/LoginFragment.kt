package com.droidteahouse.rainydays.ui.login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.droidteahouse.rainydays.R
import com.droidteahouse.rainydays.RainyDaysApplication
import com.droidteahouse.rainydays.ui.AuthFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject


class LoginFragment : AuthFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    val loginViewModel: LoginViewModel by viewModels(
        ownerProducer = { requireParentFragment() },
        factoryProducer = { factory })

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as RainyDaysApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //remove navigation up
        if (loginViewModel.isLoggedIn()) {
            loginViewModel.logout()
        }
        login.isEnabled = !formEmpty()
        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                link_signup.isEnabled =
                    !formEmpty() && loginFormState.isDataValid
                login.isEnabled =
                    !formEmpty() && loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    username.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    password.error = getString(it)
                }
            })

        loginViewModel.loginResult.observe(
            viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loading.visibility = View.GONE
                when (loginResult.status) {
                    Status.FAILED -> showLoginFailed(loginResult.msg!!)
                    Status.SUCCESS -> updateUiWithUser(loginResult.data as FirebaseUser)
                }
                loginViewModel.loginResult.postValue(null)
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }
        }
        username.addTextChangedListener(afterTextChangedListener)
        password.addTextChangedListener(afterTextChangedListener)
        password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    username.text.toString(),
                    password.text.toString()
                )
            }
            false
        }

        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            loginViewModel.login(
                username.text.toString(),
                password.text.toString()
            )
        }

        link_signup.setOnClickListener {
            if (!formEmpty()) {
                    loading.visibility = View.VISIBLE

                    loginViewModel.register(
                        username.text.toString(),
                        password.text.toString()
                    )
                }
        }

    }

    private fun formEmpty() = (username?.text?.length == 0) && password?.text?.length == 0

    private fun updateUiWithUser(model: FirebaseUser) {
        val welcome = getString(R.string.welcome) + model.email
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Snackbar.make(requireView(), welcome, Snackbar.LENGTH_LONG).show()

        findNavController().navigate(R.id.nav_tictactoe)
    }

    private fun showLoginFailed(errorString: String) {
        val appContext = context?.applicationContext ?: return
        Snackbar.make(requireView(), errorString, Snackbar.LENGTH_LONG).show()
    }
}
