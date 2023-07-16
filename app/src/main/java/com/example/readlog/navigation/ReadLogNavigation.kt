package com.example.readlog.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.readlog.screens.ReadLogSplashScreen
import com.example.readlog.screens.details.BookDetailsScreen
import com.example.readlog.screens.home.HomeScreen
import com.example.readlog.screens.home.HomeScreenViewModel
import com.example.readlog.screens.login.ReadLogLoginScreen
import com.example.readlog.screens.search.BookSearchViewModel
import com.example.readlog.screens.search.SearchScreen
import com.example.readlog.screens.stats.ReadLogStatsScreen
import com.example.readlog.screens.update.BookUpdateScreen

@Composable
fun ReadLogNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = ReadLogScreens.SplashScreen.name){
        composable(ReadLogScreens.SplashScreen.name){
            ReadLogSplashScreen(navController = navController)
        }
        composable(ReadLogScreens.ReadLogHomeScreen.name){
            val homeScreenViewModel = hiltViewModel<HomeScreenViewModel>()
            HomeScreen(navController = navController,homeScreenViewModel)
        }
        composable(ReadLogScreens.ReadLogStateScreen.name){
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            ReadLogStatsScreen(navController = navController,homeViewModel)
        }

        val updateName = ReadLogScreens.UpdateScreen.name
        composable("$updateName/{bookItemId}", arguments = listOf(navArgument("bookItemId"){
            type =  NavType.StringType
        })){backStack ->
            backStack.arguments?.getString("bookItemId").let {
                BookUpdateScreen(navController = navController,bookItemId = it.toString())
            }

        }
        val detailName = ReadLogScreens.DetailScreen.name
        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId"){
            type = NavType.StringType
        })){backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {
                BookDetailsScreen(navController = navController, bookId = it.toString())
            }

        }
        composable(ReadLogScreens.SearchScreen.name){
            val viewModel = hiltViewModel<BookSearchViewModel>()
            SearchScreen(navController = navController,viewModel)
        }
        composable(ReadLogScreens.LoginScreen.name){
            ReadLogLoginScreen(navController = navController)
        }

    }

}