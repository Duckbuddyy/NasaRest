package com.duckbuddyy.nasarest.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.duckbuddyy.nasarest.databinding.ActivityMainBinding
import com.duckbuddyy.nasarest.domain.RoverType
import com.duckbuddyy.nasarest.ui.home.PhotoFragment
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var _viewPager: ViewPager
    private lateinit var _tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            _viewPager = view_pager
            _tabLayout = tab_layout
        }

        val curiosityFragment = PhotoFragment(RoverType.Curiosity)
        val opportunityFragment = PhotoFragment(RoverType.Opportunity)
        val spiritFragment = PhotoFragment(RoverType.Spirit)

        _tabLayout.setupWithViewPager(_viewPager)

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, 0)
        viewPagerAdapter.addFragment(curiosityFragment, "CURIOSITY")
        viewPagerAdapter.addFragment(opportunityFragment, "OPPORTUNITY")
        viewPagerAdapter.addFragment(spiritFragment, "SPIRIT")
        _viewPager.adapter = viewPagerAdapter

    }
}