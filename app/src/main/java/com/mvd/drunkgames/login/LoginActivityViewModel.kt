package com.mvd.drunkgames.login

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth




class LoginActivityViewModel(application: Application) : AndroidViewModel(application) {

//    private lateinit var googleSignInOptions: GoogleSignInOptions
//    private lateinit var context: Context
//    private var mAuth: FirebaseAuth? = null
//
//
//    init {
//        context = getApplication()
//    }
//
//    fun initSignIn() {
////        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
////            .requestIdToken(R.string.default_client_id.toString())
////            .requestEmail()
////            .build()
//        mAuth = FirebaseAuth.getInstance()
//    }
//
//    fun onSignResult(data: Intent?){
//        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//        try {
//            val account = task.getResult(ApiException::class.java)
//            firebaseAuthWithGoogle(account!!)
//        } catch (e: ApiException) {
//            Log.w("AAA", "Google sign in failed", e)
//        }
//    }
//
//    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
//        // [START_EXCLUDE silent]
//       // showProgressDialog()
//        // [END_EXCLUDE]
//
//        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
//        mAuth!!.signInWithCredential(credential)
//            .addOnCompleteListener(this.context,
//                OnCompleteListener<AuthResult> { task ->
//                    if (task.isSuccessful) {
//                        // Sign in success, update UI with the signed-in user's information
//                        val user = mAuth.getCurrentUser()
//                        updateUI(user)
//                    } else {
//                        // If sign in fails, display a message to the user.
////                        Snackbar.make(
////                            findViewById(R.id.main_layout),
////                            "Authentication Failed.",
////                            Snackbar.LENGTH_SHORT
////                        ).show()
//                        updateUI(null)
//                    }
//
//                    // [START_EXCLUDE]
//             //       hideProgressDialog()
//                    // [END_EXCLUDE]
//                })
//    }

}