package com.example.readlog.screens.search

import android.graphics.fonts.FontStyle
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.readlog.components.InputField
import com.example.readlog.components.ReadLogAppBar
import com.example.readlog.model.Item
import com.example.readlog.navigation.ReadLogScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: BookSearchViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        ReadLogAppBar(
            title = "Search Books",
            navController = navController,
            icon = Icons.Default.ArrowBack,
            showProfile = false
        ) {
            navController.navigate(ReadLogScreens.ReadLogHomeScreen.name)
        }
    }) {

        Box(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Surface {
                Column() {
                    SearchForm(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        viewModel = viewModel
                    ) {query ->
                        viewModel.searchBooks(query)

                    }
                    Spacer(modifier = Modifier.height(13.dp))
                    BookList(navController,viewModel)
                }
            }
        }

    }
}

@Composable
fun BookList(navController: NavController,
             viewModel: BookSearchViewModel= hiltViewModel()) {


    val listOfBooks = viewModel.list

    if (viewModel.isLoading)
        LinearProgressIndicator()
    else
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items = listOfBooks) { book ->
                BookRow(book, navController)
            }

        }

}

@Composable
fun BookRow(
    book: Item,
    navController: NavController,

) {
    Card(modifier = Modifier
        .clickable {
            navController.navigate(ReadLogScreens.DetailScreen.name+ "/${book.id}")
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
            val imageUrl:String = if(book.volumeInfo.imageLinks.smallThumbnail.isEmpty())
                "https://dictionary.cambridge.org/images/full/book_noun_001_01679.jpg?version=5.0.326"
            else {
                Log.d("Image", "Link -> ${book.volumeInfo.imageLinks.smallThumbnail}")
                book.volumeInfo.imageLinks.smallThumbnail}
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(80.dp)
                    .padding(end = 2.dp)
            )
            Column() {
                Text(
                    text = book.volumeInfo.title,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "Author: ${book.volumeInfo.authors}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Date: ${book.volumeInfo.publishedDate}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "${book.volumeInfo.categories}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    viewModel: BookSearchViewModel,
    onSearch: (String) -> Unit = {}
) {
    Column {
        val searchQueryState = rememberSaveable {
            mutableStateOf("")
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()
        }
        InputField(valueState = searchQueryState, labelId = "Search",
            enabled = true,
            onAction = KeyboardActions {

                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())

                searchQueryState.value = ""
                keyboardController?.hide()
            }
        )
    }
}