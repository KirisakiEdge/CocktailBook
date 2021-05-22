package com.example.cocktailbook.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.example.cocktailbook.R
import com.example.cocktailbook.databinding.ItemDrinkBinding
import com.example.cocktailbook.model.Drink

class DrinkListAdapter(private val drinks: MutableList<Drink>, private  val findNavController: NavController):
    RecyclerView.Adapter<DrinkListAdapter.HomeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDrinkBinding.inflate(layoutInflater, parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = drinks[position]
        holder.drinkName.text = item.strDrink
        holder.drinkImage.load(item.strDrinkThumb){
            transformations(RoundedCornersTransformation(25f))
            scale(Scale.FILL)
        }
        holder.itemView.setOnClickListener {
            when (findNavController.currentDestination!!.label) {
                "Home" -> {
                    findNavController.navigate(R.id.action_navigation_home_to_drinkDetailsFragment2,
                            bundleOf("idDrink" to item.idDrink,
                                    "label" to findNavController.currentDestination!!.label))
                }
                "User" -> {
                    findNavController.navigate(R.id.action_navigation_user_to_drinkDetailsFragment2,
                            bundleOf("idDrink" to item.idDrink,
                                    "label" to findNavController.currentDestination!!.label))
                }
                "Search" -> {
                    findNavController.navigate(R.id.action_navigation_searchDrink_to_drinkDetailsFragment2,
                            bundleOf("idDrink" to item.idDrink,
                                    "label" to findNavController.currentDestination!!.label))
                }
            }
        }
    }
    fun updateData(drink: MutableList<Drink>) {
        this.drinks.clear()
        this.drinks.addAll(drink)
        Log.e("drinklistadpater", drinks.toString())
        notifyDataSetChanged()
    }


    override fun getItemCount() = drinks.size

    class HomeViewHolder(view : ItemDrinkBinding): RecyclerView.ViewHolder(view.root) {
        val drinkName: TextView = view.drinkName
        val drinkImage: ImageView = view.drinkImage
    }

}