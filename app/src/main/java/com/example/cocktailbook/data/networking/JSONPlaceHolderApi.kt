package com.example.cocktailbook.data.networking

import com.example.cocktailbook.model.Drink
import com.example.cocktailbook.model.DrinksList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface JSONPlaceHolderApi {
    @GET("search.php")
    fun getDrinksByName(@Query("s") drinksName:String): Call<DrinksList>

    @GET("lookup.php")
    fun getDrinkById(@Query("i") idDrink: String): Call<DrinksList>

    @GET("filter.php")
    fun nonAlcoholicFilter(@Query("a") nonAlcoholic: String = "Non_Alcoholic"): Call<DrinksList>
}