package com.example.cocktailbook.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cocktailbook.model.Drink
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Drink::class], version = 1)
@TypeConverters(IngredientsConverter::class)
abstract class DataBase: RoomDatabase() {

    abstract fun dao(): DAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: DataBase? = null

        fun getDatabase(context: Context): DataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        DataBase::class.java,
                        "database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}