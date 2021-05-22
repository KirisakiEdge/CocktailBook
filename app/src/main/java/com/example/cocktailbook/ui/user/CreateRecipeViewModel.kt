package com.example.cocktailbook.ui.user

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CreateRecipeViewModel: ViewModel() {

    private val _ingredients =  MutableStateFlow<MutableList<String>?>(null)
    fun ingredients(): StateFlow<MutableList<String>?> = _ingredients

    private val _measure =  MutableStateFlow<MutableList<String>?>(null)
    fun measure(): StateFlow<MutableList<String>?> = _measure

    var ingredients: MutableList<String>?
        get() = _ingredients.value
        set(ingredients) {
            _ingredients.value = ingredients
        }

    var measure: MutableList<String>?
        get() = _measure.value
        set(measure) {
            _measure.value = measure
        }
}