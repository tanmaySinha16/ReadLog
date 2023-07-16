package com.example.readlog.di

import com.example.readlog.network.BooksApi
import com.example.readlog.repository.BookRepository
import com.example.readlog.repository.FireRepository
import com.example.readlog.utils.Constants.BASE_URL
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBookApi():BooksApi
            = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BooksApi::class.java)

    @Singleton
    @Provides
    fun provideBookRepository(api:BooksApi) = BookRepository(api)

    @Singleton
    @Provides
    fun provideFireBookRepository()
    =FireRepository(queryBook = FirebaseFirestore.getInstance()
        .collection("books")
    )

}