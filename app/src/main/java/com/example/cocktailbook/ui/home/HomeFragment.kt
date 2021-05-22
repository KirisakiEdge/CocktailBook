package com.example.cocktailbook.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cocktailbook.R
import com.example.cocktailbook.data.CocktailApplication
import com.example.cocktailbook.databinding.FragmentHomeBinding
import com.example.cocktailbook.ui.adapter.DrinkListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var adapter: DrinkListAdapter? = null
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory((requireActivity().application as CocktailApplication).repository)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        lifecycleScope.launch(Dispatchers.IO){
            launch {
                homeViewModel.drinkListStateFlow().collect { drinkList -> drinkList?.let {
                    Log.e("homeFrag", it.toString())
                    if (drinkList.isNotEmpty()){
                        launch(Dispatchers.Main) {
                            binding.deleteDrinks.visibility = View.VISIBLE
                            binding.empty.visibility = View.GONE
                            adapter =  DrinkListAdapter(drinkList, findNavController())
                            binding.drinksRecycleView.adapter = adapter
                        }
                    }
                }
            }
            }
        }
        binding.deleteDrinks.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){  homeViewModel.deleteAllDrinks() }
            adapter!!.updateData(mutableListOf())
            binding.empty.visibility = View.VISIBLE
            binding.deleteDrinks.visibility = View.INVISIBLE
        }
    }
}