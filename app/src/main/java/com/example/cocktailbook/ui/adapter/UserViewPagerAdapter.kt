package com.example.cocktailbook.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cocktailbook.ui.user.FavoriteListFragment
import com.example.cocktailbook.ui.user.OwnListFragment

class UserViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    // this is for fragment tabs
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteListFragment()
            1 -> OwnListFragment()
            else -> FavoriteListFragment()
        }
    }
    override fun getItemCount() = 2
}