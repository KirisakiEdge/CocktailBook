package com.example.cocktailbook.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SessionManager (context: Context) {
    private var userIsLogin: SharedPreferences = context.getSharedPreferences("loginIn", Context.MODE_PRIVATE)
    private var isAlcoholic: SharedPreferences = context.getSharedPreferences("isAlcoholic", Context.MODE_PRIVATE)
    private var nightMode: SharedPreferences = context.getSharedPreferences("nightMode", Context.MODE_PRIVATE)

    companion object {
        const val LOG_IN = "LOG_IN"
        const val IS_ALCO = "IS_ALCO"
        const val NIGHT_MODE = "NIGHT_MODE"
    }


    fun saveUserStatus(userLogged: Boolean) {
        val authEditor = userIsLogin.edit()
        authEditor.putBoolean(LOG_IN, userLogged)
        authEditor.apply()
    }


    fun fetchUserStatus(): Boolean {
        val userStatus = userIsLogin.getBoolean(LOG_IN, false)
        Log.e("manager", userStatus.toString())
        return userStatus
    }


    fun setAlcoholic(isAlco: Boolean) {
        val alcoEditor = isAlcoholic.edit()
        alcoEditor.putBoolean(IS_ALCO, isAlco)
        alcoEditor.apply()
    }

    fun isAlcoholic(): Boolean {
        val alcoFilter = isAlcoholic.getBoolean(IS_ALCO, false)
        Log.e("manager", alcoFilter.toString())
        return alcoFilter
    }

    fun setNightMode(nightM: Int) {
        val themeEditor = nightMode.edit()
        themeEditor.putInt(NIGHT_MODE, nightM)
        themeEditor.apply()
    }

    fun isNightMode(): Int {
        val themeEditor = nightMode.getInt(NIGHT_MODE, 1)
        Log.e("manager", themeEditor.toString())
        return themeEditor
    }
}