package com.example.cocktailbook.ui.user

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.cocktailbook.R
import com.example.cocktailbook.data.SessionManager
import com.example.cocktailbook.databinding.FragmentUserBinding
import com.example.cocktailbook.ui.adapter.UserViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class UserFragment : Fragment(R.layout.fragment_user) {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setHasOptionsMenu(true)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(UserFragmentDirections.actionNavigationUserToNavigationHome())
                // in here you can do logic when backPress is clicked
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserBinding.bind(view)
        userViewModel = ViewModelProvider(this, UserViewModelFactory()).get(UserViewModel::class.java)

        binding.viewPager2.adapter = UserViewPagerAdapter(requireActivity())
        binding.viewPager2.isSaveEnabled = false


        TabLayoutMediator(binding.viewPager2Tabs, binding.viewPager2) { tab, position ->
            if (position == 0) {
                tab.text = "Favorite"
            } else {
                tab.text = "Owns"
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.user_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val alcoholic = menu.findItem(R.id.action_alcoholic)
        val nightMode = menu.findItem(R.id.action_theme)
        alcoholic.isChecked = !SessionManager(requireContext()).isAlcoholic()
        if (SessionManager(requireContext()).isNightMode() == MODE_NIGHT_YES) {
            nightMode.isChecked = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                findNavController().navigate(UserFragmentDirections.actionNavigationUserToNavigationHome())
            }
            R.id.action_alcoholic -> {
                SessionManager(requireContext()).setAlcoholic(item.isChecked) //without ! not work
            }
            R.id.action_theme -> {
                if (SessionManager(requireContext()).isNightMode() == MODE_NIGHT_NO) {
                    SessionManager(requireContext()).setNightMode(MODE_NIGHT_YES)
                    setDefaultNightMode(MODE_NIGHT_YES)
                }else{
                    SessionManager(requireContext()).setNightMode(MODE_NIGHT_NO)
                    setDefaultNightMode(MODE_NIGHT_NO)
                }
            }
            R.id.action_exit -> dialogExit().show()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogExit(): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Are you sure?")
        builder.setPositiveButton("Ok and LogOut") { _, _ ->
            SessionManager(requireContext()).saveUserStatus(false)
            requireActivity().finish()
        }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        return builder.create()
    }

}

