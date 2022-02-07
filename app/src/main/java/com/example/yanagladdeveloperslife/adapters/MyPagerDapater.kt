package com.example.yanagladdeveloperslife.adapters

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.yanagladdeveloperslife.R
import com.example.yanagladdeveloperslife.fragments.FavouritesFragment

import com.example.yanagladdeveloperslife.fragments.RandomFragment
import com.example.yanagladdeveloperslife.fragments.RecyclerFragment

class MyPagerAdapter(private val context: Context, manager: FragmentManager?) : FragmentPagerAdapter(manager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private lateinit var randomFragment: RandomFragment
    private lateinit var topFragment: RecyclerFragment
    private lateinit var latestFragment: RecyclerFragment
    private lateinit var favouritesFragment: FavouritesFragment

    val currentFragment: Fragment?
        get() {
            if (randomFragment.isOnScreen) return randomFragment
            if (topFragment.isOnScreen) return topFragment
            if(favouritesFragment.isOnScreen) return favouritesFragment
            return if (latestFragment.isOnScreen) latestFragment else null
        }

    override fun setPrimaryItem(container: ViewGroup, position: Int, obj: Any) {

        if (randomFragment.isOnScreen && randomFragment !== obj) {
            randomFragment.isOnScreen = false
        }
        if (topFragment.isOnScreen && topFragment !== obj) {
            topFragment.isOnScreen = false
        }
        if (latestFragment.isOnScreen && latestFragment !== obj) {
            latestFragment.isOnScreen = false
        }
        if (favouritesFragment.isOnScreen && favouritesFragment !== obj) {
            favouritesFragment.isOnScreen = false
        }
        when (position) {
            0 -> randomFragment.isOnScreen = true
            1 -> topFragment.isOnScreen = true
            2 -> latestFragment.isOnScreen = true
            3 -> favouritesFragment.isOnScreen = true
        }

        super.setPrimaryItem(container, position, obj)
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                randomFragment = RandomFragment()
                return randomFragment
            }
            1 -> {
                topFragment = RecyclerFragment.newInstance("latest")
                return topFragment
            }
            2 -> {
                latestFragment = RecyclerFragment.newInstance("top")
                return latestFragment
            }
            3 -> {
                favouritesFragment = FavouritesFragment.newInstance()
                return favouritesFragment
            }
        }
        Log.e("PagerAdapter getItem", "Error at position$position")
        return RandomFragment()
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return context.getString(R.string.random)
            1 -> return context.getString(R.string.latest)
            2 -> return context.getString(R.string.top)
            3 -> return context.getString(R.string.favourite)
        }
        Log.e("PagerAdapter getItem", "Error at position$position")
        return context.getString(R.string.random)
    }

    override fun getCount(): Int {
        return 4
    }
}
