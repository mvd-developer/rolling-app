package com.mvd.drunkgames.login

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mvd.drunkgames.MainActivity
import com.mvd.drunkgames.R
import com.mvd.drunkgames.preferences.PrefsManager


class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 9001

    private lateinit var viewModel: LoginActivityViewModel

    private lateinit var signInButton: SignInButton
    private lateinit var startGameButton: LottieAnimationView
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this)[LoginActivityViewModel::class.java]

        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        mAuth = FirebaseAuth.getInstance()

        signInButton = findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener(this::signIn)

        startGameButton = findViewById(R.id.btn_start)
        startGameButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

    private fun signIn(view: View) {
        val signInIntent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun startGame(id: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, id)
        }
        startActivity(intent)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.e("AAA", e.toString())

            }

        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this,
                        OnCompleteListener<AuthResult> { task ->
                            if (task.isSuccessful) {
                                val user = mAuth!!.getCurrentUser()

                                //login complete
                                if (user != null) {
                                    startGame(user.uid)
                                    PrefsManager.setUserId(user.uid);
                                }

                            } else {

                            }

                        })
    }

}