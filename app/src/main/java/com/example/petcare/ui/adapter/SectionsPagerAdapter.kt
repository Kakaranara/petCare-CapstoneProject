package com.example.petcare.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.petcare.ui.main.news.AllNewsFragment
import com.example.petcare.ui.main.news.NewsFragment

class SectionsPagerAdapter(newsFragment: NewsFragment) : FragmentStateAdapter(newsFragment) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        /*
        * Commented for possibility using it in the future, do not erase it just yet.
        * */
//        var fragment: Fragment? = null
//        when (position) {
//            0 -> fragment = AllNewsFragment()
//            1 -> fragment = HealthNewsFragment()
//            2 -> fragment = AllNewsFragment()
//            3 -> fragment = HealthNewsFragment()
//        }
//        return fragment as Fragment

        val fragment = AllNewsFragment()
        fragment.arguments = Bundle().apply {
            putInt(AllNewsFragment.ARG_SECTION_NUMBER, position + 1)
        }
        return fragment
    }

}