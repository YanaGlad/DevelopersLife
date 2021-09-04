package com.example.yanagladdeveloperslife


import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.yanagladdeveloperslife.adapters.MyPagerAdapter
import com.example.yanagladdeveloperslife.databinding.ActivityMainBinding
import com.example.yanagladdeveloperslife.fragments.RecyclerFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var myPagerAdapter: MyPagerAdapter
    private var _binding: ActivityMainBinding? = null
    internal val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPagerAdapter = MyPagerAdapter(applicationContext, supportFragmentManager)

        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.adapter = myPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)

    }
}

