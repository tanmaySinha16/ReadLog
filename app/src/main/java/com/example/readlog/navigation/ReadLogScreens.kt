package com.example.readlog.navigation

import androidx.compose.runtime.Composable
import androidx.room.Update
import java.lang.IllegalArgumentException

enum class ReadLogScreens {
    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    ReadLogHomeScreen,
    SearchScreen,
    DetailScreen,
    UpdateScreen,
    ReadLogStateScreen,;
    companion object{
        fun fromRoute(route:String?) : ReadLogScreens
        = when(route?.substringBefore("/")){
            SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            CreateAccountScreen.name -> CreateAccountScreen
            SearchScreen.name -> SearchScreen
            DetailScreen.name -> DetailScreen
            UpdateScreen.name -> UpdateScreen
            ReadLogStateScreen.name -> ReadLogStateScreen
            null -> ReadLogHomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}
