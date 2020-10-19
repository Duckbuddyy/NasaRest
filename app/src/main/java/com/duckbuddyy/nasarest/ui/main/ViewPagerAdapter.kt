package com.duckbuddyy.nasarest.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(
    private val fragmentManager: FragmentManager, private val behavior: Int,
) : FragmentPagerAdapter(fragmentManager, behavior) {

    private val tabList = arrayListOf<Fragment>()
    private val tabTitle = arrayListOf<String>()

    fun addFragment(fragment: Fragment, title:String){
        tabList.add(fragment)
        tabTitle.add(title)
    }

    override fun getCount(): Int = tabList.size

    override fun getItem(position: Int): Fragment = tabList[position]

    override fun getPageTitle(position: Int): CharSequence? = tabTitle[position]

}