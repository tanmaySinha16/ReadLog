package com.example.readlog.repository

import com.example.readlog.data.DataOrException
import com.example.readlog.data.Resource
import com.example.readlog.model.Item
import com.example.readlog.network.BooksApi
import java.lang.Exception
import javax.inject.Inject

class BookRepository@Inject constructor(
    private val api:BooksApi
) {
    private val dataOrException = DataOrException<List<Item>, Boolean, Exception>()
    private val bookInfoDataOrException = DataOrException<Item, Boolean, Exception>()


    suspend fun getBooks(searchQuery: String):
            Resource<List<Item>> {
        return try {
            Resource.Loading(true)
            val itemList = api.getAllBooks(query = searchQuery).items
            if(itemList.isNotEmpty())
                Resource.Loading(false)
            Resource.Success(data = itemList)

        } catch (e: Exception) {
            return Resource.Error(message = e.message.toString())
        }

    }

    suspend fun getBookInfo(bookId: String): Resource<Item> {
        val response = try {
            Resource.Loading(true)
            api.getBookInfo(bookId)

        } catch (e: Exception) {
            return Resource.Error(message = "An error occurred ${e.message.toString()}")
        }
        Resource.Loading(false)
        return Resource.Success(data = response)

    }
}