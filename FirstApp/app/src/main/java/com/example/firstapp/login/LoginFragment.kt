package com.example.firstapp.login

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.from
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firstapp.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.safetynet.SafetyNetClient
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.concurrent.Executors

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {
    private val callbackManager = CallbackManager.Factory.create()
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var myReCaptchaClient: SafetyNetClient? = null
    private var account: GoogleSignInAccount? = null
    private val executor = Executors.newSingleThreadExecutor()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInWithGoogle()
        createClientToReCaptcha()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_login, container, false)

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        facebook_button.setOnClickListener {
            loginWithFacebook()
        }
        google_button.setOnClickListener {
            signIn()
        }
        fingerprint_button.setOnClickListener {
            checkBiometrics()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    override fun onResume() {
        super.onResume()

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        account = GoogleSignIn.getLastSignedInAccount(requireContext())

        if(account != null || isLoggedIn){
            if(!checkCredentials().isNullOrEmpty()) LoginFragmentDirections.navigateToTasks()
        }

    }

    private fun loginWithFacebook(){
        facebook_button.setPermissions("email")
        facebook_button.authType = "rerequest"
        facebook_button.fragment = this

        facebook_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                saveUser(loginResult?.accessToken?.userId)
                Toast.makeText(requireContext(), "Login success", Toast.LENGTH_LONG).show()
                findNavController().navigate(LoginFragmentDirections.navigateToTasks())
            }
            override fun onCancel() {
                Toast.makeText(requireContext(), "Cancel", Toast.LENGTH_LONG).show()
            }
            override fun onError(exception: FacebookException) {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_key_sign_in))
            .requestEmail()
            .build()
        //Sign in object
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            if (completedTask.isSuccessful) {
                saveUser(completedTask.result?.givenName)
                Toast.makeText(requireContext(), "Login with google successfully", Toast.LENGTH_LONG).show()
                validateCaptcha()
            }
        } catch (e: ApiException) {
            Log.e("failed code=", e.statusCode.toString())
        }
    }

    private fun createClientToReCaptcha() {
        myReCaptchaClient = SafetyNet.getClient(requireActivity())
    }

    private fun validateCaptcha() {
        SafetyNet.getClient(requireActivity())
            .verifyWithRecaptcha(getString(R.string.my_site_key))
            .addOnSuccessListener(requireActivity()) { response ->
                val userResponseToken = response.tokenResult
                if (!userResponseToken.isNullOrEmpty()) {
                    // Validate the user response token using the reCAPTCHA siteverify API.
                    //No aplicable la verificacion con backend
                    findNavController().navigate(LoginFragmentDirections.navigateToTasks())
                }
            }
            .addOnFailureListener(requireActivity()) { e ->
                if (e is ApiException) {
                    Log.d(
                        ContentValues.TAG,
                        "Error: ${CommonStatusCodes.getStatusCodeString(e.statusCode)}"
                    )
                } else {
                    Log.d(ContentValues.TAG, "Error: ${e.message}")
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkBiometrics() {
        when (from(requireContext()).canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> apply {
                Log.d("Success", "App can authenticate using biometrics.")
                showBiometricPrompt()
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e("Error", "No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e("Error", "Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Log.e(
                    "Error", "The user hasn't associated any biometric credentials " +
                            "with their account."
                )
        }
    }

    private fun showBiometricPrompt() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Log.d(ContentValues.TAG, "Authentication error.")
                    requireActivity().runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            "Error. Try again!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    requireActivity().runOnUiThread {
                        findNavController().navigate(LoginFragmentDirections.navigateToTasks())
                    }
                }
            })
        biometricPrompt.authenticate(promptInfo)
    }

    fun checkCredentials(): String? {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        return sharedPref?.getString("Name", null)
    }

    private fun saveUser(name: String?) {
        if (!name.isNullOrEmpty()) {
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putString("Name", name)
                apply()
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
