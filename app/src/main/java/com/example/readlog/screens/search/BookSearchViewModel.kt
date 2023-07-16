package com.example.readlog.screens.search

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readlog.data.DataOrException
import com.example.readlog.data.Resource
import com.example.readlog.model.Item
import com.example.readlog.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel@Inject constructor(
    private val repository: BookRepository
):ViewModel() {
    var list:List<Item> by mutableStateOf(listOf())
    var isLoading:Boolean by mutableStateOf(true)
    init {
        searchBooks("flutter")
    }

     fun searchBooks(query: String) {
        viewModelScope.launch {
            isLoading = true
            if(query.isEmpty())
                return@launch
            try {
                when(val response = repository.getBooks(query)){
                    is Resource.Success -> {
                        list = response.data!!
                        if(list.isNotEmpty())
                        isLoading = false
                    }

                    is Resource.Error ->{
                        isLoading = false
                        Log.e("Network","Failed getting books")
                    }
                    else -> {isLoading = false}

                }
            }catch (e:Exception)
            {
                Log.d(TAG, "searchBooks: ${e.message}")
            }


        }
    }
}