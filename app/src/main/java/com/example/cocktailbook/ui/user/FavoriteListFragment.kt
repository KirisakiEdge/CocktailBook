package com.example.cocktailbook.ui.user

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cocktailbook.R
import com.example.cocktailbook.data.networking.NetworkService
import com.example.cocktailbook.databinding.FragmentFavoriteListBinding
import com.example.cocktailbook.model.Drink
import com.example.cocktailbook.model.DrinksList
import com.example.cocktailbook.ui.adapter.DrinkListAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class FavoriteListFragment : Fragment(R.layout.fragment_favorite_list) {

    private var _binding : FragmentFavoriteListBinding? = null
    private val binding get() = _binding!!
    private var adapter: DrinkListAdapter? = null
    private var listOfDrink : MutableList<Drink> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteListBinding.bind(view)
        adapter = DrinkListAdapter(mutableListOf(), findNavController())
        binding.favoriteRecipesList.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO){
            val reference = Firebase.database.getReference("${Firebase.auth.uid}/Favorites")
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        val snapshotIterator = snapshot.children
                        val iterator = snapshotIterator.iterator()
                        while (iterator.hasNext()) {
                            listOfDrink.add(iterator.next().getValue<Drink>()!!)
                        }
                        adapter?.updateData(listOfDrink)
                        binding.userFavoriteListLoading.visibility = View.GONE
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }
}