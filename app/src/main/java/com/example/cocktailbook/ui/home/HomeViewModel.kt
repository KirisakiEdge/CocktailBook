package com.example.cocktailbook.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.cocktailbook.data.CocktailRepository
import com.example.cocktailbook.model.Drink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: CocktailRepository) : ViewModel() {

    private var _drinkList: MutableStateFlow<MutableList<Drink>?> = MutableStateFlow(mutableListOf())
    init {
        viewModelScope.launch {
            repository.drinkStateFlow.collect {
                _drinkList.value = it }
        }
    }

    fun drinkListStateFlow(): StateFlow<MutableList<Drink>?> = _drinkList

    suspend fun drinkById(id: String): Drink {
        return repository.loadDrinkById(id)
    }

    fun deleteAllDrinks() = viewModelScope.launch { repository.deleteAllDrink() }
}
class HomeViewModelFactory(private val repository: CocktailRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}