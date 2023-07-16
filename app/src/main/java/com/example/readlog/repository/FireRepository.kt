package com.example.readlog.repository


import android.util.Log
import com.example.readlog.data.DataOrException
import com.example.readlog.data.Resource
import com.example.readlog.model.MBook
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(
    private val queryBook:Query
){
    suspend fun getAllBooksFromDatabase():DataOrException<List<MBook>,Boolean , Exception>{
        val dataOrException = DataOrException<List<MBook>,Boolean, Exception>()
        try{
            dataOrException.loading = true
            dataOrException.data = queryBook.get().await().documents.map {documentSnapshot ->
                documentSnapshot.toObject(MBook::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty())
                dataOrException.loading=false
        }catch (exception: FirebaseFirestoreException){
            Log.d("FB", "getAllBooksFromDatabase:${exception.message} ")
        }
        return dataOrException
    }
}