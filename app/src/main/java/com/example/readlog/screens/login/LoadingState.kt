package com.example.readlog.screens.login

import androidx.compose.runtime.Composable

data class LoadingState(
    val status:Status, val message:String?=null
){

    companion object{
        val IDLE = LoadingState(Status.IDLE)
        val FAILED = LoadingState(Status.FAILED)
        val SUCCESS = LoadingState(Status.SUCCESS)
        val LOADING = LoadingState(Status.LOADING)
    }

    enum class Status{
        SUCCESS,
        FAILED,
        LOADING,
        IDLE
    }
}


