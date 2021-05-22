package com.example.cocktailbook.data

import android.app.Application
import com.example.cocktailbook.data.db.DataBase
import com.example.cocktailbook.data.networking.NetworkService
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CocktailApplication: Application() {
    private val dataBase by lazy { DataBase.getDatabase(this) }
    private val webService by lazy { NetworkService().getService() }
    private val firebaseDataBase by lazy { Firebase.database }
    val repository by lazy { CocktailRepository(dataBase.dao(), webService) }
}