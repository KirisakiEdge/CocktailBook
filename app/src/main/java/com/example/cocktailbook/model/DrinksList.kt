package com.example.cocktailbook.model

import com.google.gson.annotations.SerializedName

class DrinksList {
    @SerializedName("drinks")
    var allDrinks: MutableList<Drink>? = null
    private lateinit var id: String


    fun getIdByPosition(position: Int):String{
        allDrinks!!.forEach{
            if(allDrinks!!.indexOf(it) == position){
                id = it.idDrink
            }
        }
        return id
    }

    fun getDrink(): Drink {
        return allDrinks!![0]
    }

    override fun toString(): String {
        return "DrinksList(allDrinks=$allDrinks)"
    }
}
