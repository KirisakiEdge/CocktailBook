package com.example.cocktailbook.data

import android.util.Log
import android.widget.Toast
import androidx.annotation.WorkerThread
import com.example.cocktailbook.data.db.DAO
import com.example.cocktailbook.data.networking.JSONPlaceHolderApi
import com.example.cocktailbook.model.Drink
import com.example.cocktailbook.model.DrinksList
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CocktailRepository(private val dao: DAO, private val webService: JSONPlaceHolderApi) {

    val drinkStateFlow = dao.drinks()

    @WorkerThread
    suspend fun saveNewDrink(drink: Drink){
        drink.allIngredients()
        drink.allMeasure()
        dao.saveNewDrink(drink)
    }

    @WorkerThread
    suspend fun loadDrinkById(id: String): Drink {
            return if (dao.loadDrinkById(id) != null) {
                Log.e("repository", dao.loadDrinkById(id).toString())
                dao.loadDrinkById(id)
            }else {
                val call = webService.getDrinkById(id)
                val drink: Drink = suspendCoroutine { continuation ->
                    call.clone().enqueue(object : Callback<DrinksList> {
                        override fun onResponse(call: Call<DrinksList>, response: Response<DrinksList>) {
                            if (response.isSuccessful) {
                                response.body()?.let { continuation.resume(it.getDrink())
                                }
                            }
                        }
                        override fun onFailure(call: Call<DrinksList>, t: Throwable) {}
                    })
                }
                saveNewDrink(drink)
                drink
            }
        }

    @WorkerThread
    suspend fun deleteAllDrink()= dao.deleteAllDrinks()

}
