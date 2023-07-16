package com.example.readlog.screens.details

import androidx.lifecycle.ViewModel
import com.example.readlog.data.Resource
import com.example.readlog.model.Item
import com.example.readlog.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel@Inject constructor(
    private val bookRepository: BookRepository
): ViewModel() {
    suspend fun getBookInfo(bookId:String) : Resource<Item> {
        return bookRepository.getBookInfo(bookId)
    }
}