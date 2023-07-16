package com.example.readlog.screens.stats

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.readlog.components.ReadLogAppBar
import com.example.readlog.model.Item
import com.example.readlog.model.MBook
import com.example.readlog.navigation.ReadLogScreens
import com.example.readlog.screens.home.HomeScreenViewModel
import com.example.readlog.screens.search.BookRow
import com.example.readlog.utils.formatDate
import com.google.api.Distribution.BucketOptions.Linear
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadLogStatsScreen(
    navController: NavHostController,
    homeViewModel: HomeScreenViewModel = hiltViewModel()
)  {
    var books: List<MBook> = emptyList()
    val currentUser = FirebaseAuth.getInstance().currentUser
    Scaffold(topBar = {
        ReadLogAppBar(
            title = "Book Stats",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        ){
            navController.popBackStack()
        }
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            Surface() {
                books = if (!homeViewModel.data.value.data.isNullOrEmpty()) {
                    homeViewModel.data.value.data!!.filter { mBook ->
                        (mBook.userId == currentUser?.uid)
                    }
                } else {
                    emptyList()
                }
            }
            Column {
                Row {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .padding(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.Person,
                            contentDescription = null
                        )
                    }
                    Text(
                        text = "Hi, ${
                            currentUser?.email.toString().split("@")[0].toUpperCase(
                                Locale.ROOT
                            )
                        }"
                    )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(4.dp)
                ){
                    val readBooksList:List<MBook> = if(!homeViewModel.data.value.data.isNullOrEmpty()){
                        books.filter {mBook ->
                            (mBook.userId == currentUser?.uid) && (mBook.finishedReading!=null)
                        }
                    }else {
                        emptyList()
                    }
                    val readingBooks = books.filter {
                        (it.startedReading !=null)&& (it.finishedReading == null)
                    }
                    Column(modifier = Modifier.padding(
                        start = 25.dp,
                        top = 4.dp,
                        bottom = 4.dp
                    ), horizontalAlignment = Alignment.Start) {
                        Text(text = "Your Stats")
                        Divider()
                        Text(text = "You're reading: ${readingBooks.size} books")
                        Text(text = "You've read: ${readBooksList.size} books")
                    }
                }
                if(homeViewModel.data.value.loading == true)
                {
                    LinearProgressIndicator()
                }
                else {
                    Divider()
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(16.dp)
                    ){
                        val readBooks:List<MBook> = if(!homeViewModel.data.value.data.isNullOrEmpty()){
                            homeViewModel.data.value.data!!.filter { mBook->
                                (mBook.userId == currentUser?.uid) && (mBook.finishedReading!=null)
                            }
                        }
                        else{
                            emptyList()
                        }
                        items(items = readBooks){book ->
                            BookRowStats(book = book, navController = navController)
                        }
                    }
                }
            }
        }

    }
}
@Composable
fun BookRowStats(
    book: MBook,
    navController: NavController,

    ) {
    Card(modifier = Modifier
        .clickable {
            navController.navigate(ReadLogScreens.DetailScreen.name + "/${book.id}")
        }
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),

        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(7.dp),
        colors = CardDefaults.cardColors(Color.White)) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top
        ) {
            val imageUrl:String = if(book.photoUrl.toString().isEmpty())
                "https://dictionary.cambridge.org/images/full/book_noun_001_01679.jpg?version=5.0.326"
            else {
                book.photoUrl.toString()
               }
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(80.dp)
                    .padding(end = 2.dp)
            )
            Column() {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = book.title.toString(),
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (book.rating !!>=4)
                    {
                        Spacer(modifier = Modifier.fillMaxHeight(0.8f))
                        Icon(imageVector = Icons.Default.ThumbUp, contentDescription = null , tint = Color.Green.copy(0.5f))
                    }
                    else {
                        Box{}
                    }
                }

                Text(
                    text = "Author: ${book.authors}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Started: ${formatDate(book.startedReading!!) }",
                    softWrap = true,
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Finished ${formatDate(book.finishedReading!!)}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}