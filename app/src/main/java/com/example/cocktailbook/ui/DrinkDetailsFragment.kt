package com.example.cocktailbook.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.cocktailbook.R
import com.example.cocktailbook.data.CocktailApplication
import com.example.cocktailbook.data.db.DataBase
import com.example.cocktailbook.databinding.FragmentDrinkDetailsBinding
import com.example.cocktailbook.model.Drink
import com.example.cocktailbook.ui.adapter.IngredientsAdapter
import com.example.cocktailbook.ui.home.HomeViewModel
import com.example.cocktailbook.ui.home.HomeViewModelFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DrinkDetailsFragment : Fragment(R.layout.fragment_drink_details) {

    private var _binding: FragmentDrinkDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: IngredientsAdapter
    private var idDrink: String? = null
    private var label: String? = null
    private val firebaseDatabase = Firebase.database

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory((requireActivity().application as CocktailApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDrinkDetailsBinding.bind(view)
        idDrink = requireArguments().getString("idDrink")
        label = requireArguments().getString("label")
        if(idDrink!!.contains("Own")) {
            val name = idDrink!!.substring(3)
            val reference = firebaseDatabase.getReference("${Firebase.auth.uid}/Owns/$name")
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    setView(snapshot.getValue<Drink>(), reference)
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }else{
            val reference = firebaseDatabase.getReference("${Firebase.auth.uid}/Favorites")
            if (label == "User"){
                val drinkReference = reference.child(idDrink!!)
                drinkReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.getValue<Drink>()?.let { setView(it, reference) }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
            }else {
                lifecycleScope.launch(Dispatchers.Main){
                    setView(homeViewModel.drinkById(idDrink!!), reference)
                }
            }
        }
    }



    private fun setView(drink:Drink?, reference: DatabaseReference){
        if (isAdded && drink != null){ //Return true if the fragment is currently added to its activity.
            binding.drinkImageDet.load(drink.strDrinkThumb) {
                transformations(RoundedCornersTransformation(25f))
            }
            (requireActivity() as AppCompatActivity).supportActionBar!!.title = drink.strDrink
            binding.drinkName.text = drink.strDrink
            binding.drinkAlcocholic.text = drink.strAlcoholic
            binding.drinkGlass.text = drink.strGlass
            adapter = IngredientsAdapter(drink.ingredients, drink.measure)
            binding.ingredient.adapter = adapter
            binding.drinkInstruction.text = drink.strInstructions
            if (!drink.idDrink.contains("Own")){
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        binding.favorite.isChecked = snapshot.child(drink.idDrink).exists()
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
                binding.favorite.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked)
                        reference.child(drink.idDrink).setValue(drink)
                    else
                        reference.child(drink.idDrink).setValue(null)
                }
            }else{
                binding.favorite.setButtonDrawable(R.drawable.ic_baseline_delete_outline_24)
                binding.favorite.setOnClickListener {
                    reference.setValue(null)
                    Firebase.storage.reference
                        .child("${Firebase.auth.currentUser.email}/${binding.drinkName.text}")
                        .delete()
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                findNavController().popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}