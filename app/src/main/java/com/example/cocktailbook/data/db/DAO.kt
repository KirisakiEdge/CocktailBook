package com.example.cocktailbook.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cocktailbook.model.Drink
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {

    @Query("SELECT * FROM DBDrink")
    fun drinks(): Flow<MutableList<Drink>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNewDrink(dbDrink: Drink)

    @Query("SELECT * FROM DBDrink WHERE idDrink IN (:userId)")
    suspend fun loadDrinkById(userId: String): Drink

    @Query("DELETE FROM DBDrink")
    suspend fun deleteAllDrinks()
}