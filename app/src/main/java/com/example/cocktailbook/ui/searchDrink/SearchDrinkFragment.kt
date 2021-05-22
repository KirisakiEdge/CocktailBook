package com.example.cocktailbook.ui.searchDrink

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cocktailbook.R
import com.example.cocktailbook.data.SessionManager
import com.example.cocktailbook.model.DrinksList
import com.example.cocktailbook.data.networking.NetworkService
import com.example.cocktailbook.databinding.FragmentSearchDrinkBinding
import com.example.cocktailbook.ui.adapter.DrinkListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class SearchDrinkFragment : Fragment(R.layout.fragment_search_drink) {

    private var _binding: FragmentSearchDrinkBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchDrinkViewModel: SearchDrinkViewModel
    private val TAG = "SearchActivity"
    private lateinit var drinksList: DrinksList
    private var adapter: DrinkListAdapter? = null
    lateinit var call: Call<DrinksList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO){
            _binding = FragmentSearchDrinkBinding.bind(view)
            searchDrinkViewModel = ViewModelProvider(requireActivity()).get(SearchDrinkViewModel::class.java)
            searchDrinkViewModel.drinkListStateFlow().collect {
                it?.let {
                    launch(Dispatchers.Main) {
                        binding.enterText.text = null
                        adapter = DrinkListAdapter(it, findNavController())
                        binding.drinksFoundRecyclerView.adapter = adapter
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (!SessionManager(requireContext()).isAlcoholic()) {
            (requireActivity() as AppCompatActivity).supportActionBar!!.title = "Non-Alcoholic Recipes"
            call = NetworkService().getService().nonAlcoholicFilter()
            getDrinks()
        }else {
            inflater.inflate(R.menu.search_drink_menu, menu)
            val manager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
            val searchItem: MenuItem? = menu.findItem(R.id.action_search)
            val searchView: SearchView = searchItem?.actionView as SearchView
            searchView.setSearchableInfo(manager.getSearchableInfo(requireActivity().componentName))
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    call = NetworkService().getService().getDrinksByName(query.toString())
                    getDrinks(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = true
            })

        }
    }

    fun getDrinks(query: String? = null){
        Log.e(TAG, query.toString())
        call.clone().enqueue(object : Callback<DrinksList> {
            override fun onFailure(call : Call<DrinksList>, t: Throwable) {
                Log.e(TAG, t.toString())
                Toast.makeText(requireContext(), "No network connection", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<DrinksList>, response: Response<DrinksList>) {
                response.body()?.let{ drinks ->
                    drinksList = drinks
                    try {
                        Log.e("SearchDrink", drinksList.toString())
                        searchDrinkViewModel.drinkList= drinks.allDrinks
                        adapter?.updateData(drinks.allDrinks!!)
                        binding.enterText.text = null
                    }catch (e: Exception){
                        Toast.makeText(requireContext(), "Invalid Name", Toast.LENGTH_LONG).show()
                    }

                }?: kotlin.run {
                    Log.e(TAG, response.toString())
                    Toast.makeText(requireContext(), "No network connection", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}