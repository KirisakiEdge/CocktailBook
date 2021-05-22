package com.example.cocktailbook.ui.searchDrink

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cocktailbook.model.Drink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchDrinkViewModel : ViewModel() {

    private val _drinkList: MutableStateFlow<MutableList<Drink>?> = MutableStateFlow(null)

    fun drinkListStateFlow(): StateFlow<MutableList<Drink>?> = _drinkList

    var drinkList: MutableList<Drink>?
        get() = _drinkList.value
        set(drinks) {
            _drinkList.value = drinks
        }
}