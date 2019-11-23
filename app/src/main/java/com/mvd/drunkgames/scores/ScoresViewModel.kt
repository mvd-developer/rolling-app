package com.mvd.drunkgames.scores

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.mvd.drunkgames.base.SingleLiveEvent
import com.mvd.drunkgames.entity.GameSession
import com.mvd.drunkgames.entity.User

class ScoresViewModel(application: Application) : AndroidViewModel(application) {

    private var userId: String = ""
    private var gameSessions: List<GameSession>? = null
    var db = FirebaseFirestore.getInstance()
    var adapter: ScoresAdapter? = ScoresAdapter()
    val scoresLoaded = SingleLiveEvent<ScoresAdapter>()

    fun setUserId(id: String) {
        userId = id
        val docRef = db.collection("users").document(id)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    gameSessions = document.toObject(User::class.java)?.gameSessions
                    adapter?.listData = gameSessions as MutableList<GameSession>?
                    scoresLoaded.postValue(adapter)
                } else {

                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }
}