package com.fansipan.habit.tracker.mood.screen.tutorial

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerTutorialAdapter(fragmentManager: FragmentManager, var fragmentList: MutableList<TutorialFragment>) :
    FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

}