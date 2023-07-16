package com.example.readlog.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readlog.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginScreenViewModel : ViewModel() {
    //    val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading


    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ) = viewModelScope.launch{
        if (_loading.value == false)
            _loading.value = true
        try{
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task->
                    if (task.isSuccessful) {
                        Log.d("FB", "Successfully created suer")
                        val displayName = task.result.user?.email?.split("@")?.get(0)
                        createUser(displayName)
                        home()
                    } else
                        Log.d("FB", "createWithEmailAndPassword: ${task.result}")
                    _loading.value = false
                }

        }catch (e: Exception) {
            Log.d("TAG", "createWithEmailAndPassword: ${e.message}")
        }

    }


    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ) =
        viewModelScope.launch {
            if (_loading.value == false)
                _loading.value = true
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("FB", "Successfull login ")

                            home()
                        } else
                            Log.d("FB", "signInWithEmailAndPassword: ${task.result}")
                        _loading.value = false
                    }

            } catch (e: Exception) {
                Log.d("TAG", "signInWithEmailAndPassword: ${e.message}")
            }
        }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        val user = MUser(userId = userId.toString(),
        displayName = displayName.toString(),
        avatarUrl = "",
        quote = "Life is great",
        profession = "Android Developer",
        id = null).toMap()
        FirebaseFirestore.getInstance().collection("users")
            .add(user)

    }

}