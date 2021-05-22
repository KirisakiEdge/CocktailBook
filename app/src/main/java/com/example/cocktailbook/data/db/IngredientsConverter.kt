package com.example.cocktailbook.data.db

import android.text.TextUtils
import android.util.Log
import androidx.room.TypeConverter

class IngredientsConverter {
    @TypeConverter
    fun fromIngredients(ingredients: ArrayList<String>): String {
        return TextUtils.join(", ", ingredients)

    }

    @TypeConverter
    fun toIngredients(data: String): ArrayList<String> {
        return try {
            data.replace("\\s+".toRegex(), " ").trim().split(", ") as ArrayList<String> // work only if have coma
        }catch (e: ClassCastException){
            arrayListOf(data) // if only one measure
        }

    }
}