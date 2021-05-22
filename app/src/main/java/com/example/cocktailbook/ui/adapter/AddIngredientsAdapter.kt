package com.example.cocktailbook.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.cocktailbook.databinding.ItemAddIngredientsBinding
import com.example.cocktailbook.ui.user.CreateRecipeViewModel

class AddIngredientsAdapter(var ingredients: MutableList<String>, var measures: MutableList<String>,
                            private val fActivity: FragmentActivity)
    : RecyclerView.Adapter<AddIngredientsAdapter.AddIngredientsViewHolder>() {

    private lateinit var createRecipeViewModel: CreateRecipeViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddIngredientsViewHolder {
        createRecipeViewModel = ViewModelProvider(fActivity).get(CreateRecipeViewModel::class.java)
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding= ItemAddIngredientsBinding.inflate(layoutInflater, parent, false)
        return AddIngredientsViewHolder(binding)
    }

    override fun getItemCount() = ingredients.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AddIngredientsViewHolder, position: Int) {
        holder.ingredient.addTextChangedListener {
            ingredients[position] = it.toString()

            if (ingredients[position] != "" && position == (ingredients.size-1)){
                ingredients.add("")
                measures.add("")
                Log.e("addIngAdapter", "ingredientText")
                notifyItemInserted(position+1)
            }
        }
        holder.measure.addTextChangedListener {
            measures[position] = it.toString()
        }
    }



    inner class AddIngredientsViewHolder(view: ItemAddIngredientsBinding) : RecyclerView.ViewHolder(view.root) {
        val ingredient = view.ingredient
        val measure = view.measure
    }
}