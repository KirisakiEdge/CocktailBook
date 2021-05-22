package com.example.cocktailbook.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cocktailbook.databinding.ItemIngredientsBinding

class IngredientsAdapter(var ingredients: ArrayList<String>, var measures: ArrayList<String>) :
    RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    var count = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding= ItemIngredientsBinding.inflate(layoutInflater, parent, false)
        return IngredientsViewHolder(binding)
    }

    override fun getItemCount() = ingredients.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        val ingredientItem = ingredients[position]
        ++count
        if (!ingredientItem.isNullOrEmpty()){
            holder.ingredient.text = "$count.$ingredientItem"
            var measureItem = ""
            try {
                measureItem = measures[position]
            } catch (e: IndexOutOfBoundsException) {
            }
            holder.measure.text = "$measureItem"
            Log.e("ingredientAdapter", "$ingredientItem ++ $measureItem")
        }else{
            holder.ingredient.visibility = View.GONE
            holder.measure.visibility = View.GONE
        }

    }

    inner class IngredientsViewHolder(view: ItemIngredientsBinding) : RecyclerView.ViewHolder(view.root) {
        val ingredient = view.ingredient
        val measure = view.measure
    }
}