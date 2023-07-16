package com.example.readlog.screens.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.readlog.components.FABContent
import com.example.readlog.components.ListCard
import com.example.readlog.components.ReadLogAppBar
import com.example.readlog.components.TitleSection
import com.example.readlog.model.MBook
import com.example.readlog.navigation.ReadLogScreens
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController,
               homeScreenViewModel: HomeScreenViewModel= hiltViewModel()) {
    Scaffold(
        topBar = {
            ReadLogAppBar(title = "ReadLog", navController = navController)
        },
        floatingActionButton = {
            FABContent {
                navController.navigate(ReadLogScreens.SearchScreen.name)
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        )
        {
            Surface(modifier = Modifier.fillMaxSize()) {
                HomeContent(navController,homeScreenViewModel)
            }
        }
    }
}


@Composable
fun ReadingRightNowArea(listOfBooks: List<MBook>, navController: NavController) {

    val readingNowList = listOfBooks.filter {
        it.startedReading!=null && it.finishedReading==null
    }

    HorizontalScrollableComponent(readingNowList){
        navController.navigate(ReadLogScreens.UpdateScreen.name+"/$it")
    }
}




@Composable
fun HomeContent(navController: NavController, homeScreenViewModel: HomeScreenViewModel) {


    var listOfBooks = emptyList<MBook>()
    val currentUser  = FirebaseAuth.getInstance().currentUser
    
    if(!homeScreenViewModel.data.value.data.isNullOrEmpty())
    {
        listOfBooks = homeScreenViewModel.data.value.data!!.toList().filter { mBook ->
            mBook.userId == currentUser?.uid.toString()
        }
        Log.d("GET", "HomeContent: ${listOfBooks}")
    }
    

    val currentUserName = if (!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())
        FirebaseAuth.getInstance().currentUser?.email
            ?.split("@")?.get(0)
    else
        "N/A"

    Column(
        Modifier.padding(2.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(modifier = Modifier.align(alignment = Alignment.Start)) {
            TitleSection(label = "You reading \n " + "actively right now...")

            Spacer(modifier = Modifier.fillMaxWidth(0.7f))

            Column {
                Icon(imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ReadLogScreens.ReadLogStateScreen.name)
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer)
                Text(
                    text = currentUserName!!,
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Red,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )

            }
        }
        ReadingRightNowArea(listOfBooks = listOfBooks, navController = navController)
        TitleSection(label = "Reading List")
        BookListArea(listOfBooks = listOfBooks, navController = navController)
    }
}

@Composable
fun BookListArea(listOfBooks: List<MBook>,
                 navController: NavController,

                ) {

    val addedBooks = listOfBooks.filter {
        it.startedReading == null && it.finishedReading ==null
    }

    HorizontalScrollableComponent(addedBooks){
        navController.navigate(ReadLogScreens.UpdateScreen.name+ "/$it")
    }
}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBook>,
                                  viewModel: HomeScreenViewModel= hiltViewModel(),
                                  onCardPressed:(String)->Unit) {
    val scrollState = rememberScrollState()
    Row(modifier= Modifier
        .fillMaxWidth()
        .heightIn(280.dp)
        .horizontalScroll(scrollState)
    ) {

        if (viewModel.data.value.loading == true){
            LinearProgressIndicator()
        }else{
            if(listOfBooks.isNullOrEmpty())
            {
                Surface(modifier = Modifier.padding(23.dp)) {
                    Text(text = "No books found. Add a book.",
                        color = Color.Red.copy(alpha = 0.4f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
            else
            {
                for(book in listOfBooks){
                    ListCard(book){
                        onCardPressed(book.googleBookId.toString())
                    }
                }
            }

        }


    }
}




