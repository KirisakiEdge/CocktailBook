package com.example.cocktailbook.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cocktailbook.R
import com.example.cocktailbook.databinding.FragmentFavoriteListBinding
import com.example.cocktailbook.databinding.FragmentOwnListBinding
import com.example.cocktailbook.model.Drink
import com.example.cocktailbook.ui.adapter.DrinkListAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class OwnListFragment : Fragment(R.layout.fragment_own_list) {

    private var _binding : FragmentOwnListBinding? = null
    private val binding get() = _binding!!
    private var adapter: DrinkListAdapter? = null
    private var listOfDrink : MutableList<Drink> = mutableListOf()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOwnListBinding.bind(view)
        adapter = DrinkListAdapter(mutableListOf(), findNavController())
        binding.ownRecipesList.adapter = adapter
        binding.addNewRecipe.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_user_to_createRecipeFragment)
        }

        lifecycleScope.launch(Dispatchers.IO){
            val reference = Firebase.database.getReference("${Firebase.auth.uid}/Owns")
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        val snapshotIterator = snapshot.children
                        val iterator = snapshotIterator.iterator()
                        while (iterator.hasNext()) {
                            listOfDrink.add(iterator.next().getValue<Drink>()!!)
                        }
                        adapter?.updateData(listOfDrink)
                        binding.userOwnListLoading.visibility = View.GONE
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }

    }

}