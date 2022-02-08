package com.example.instareelsdownloader

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

 class PagerAdapter(
    private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int
) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {

            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return reels_fragment()
            }

            1 -> {
                return videos_fragment()
            }

            2 -> {
                // val movieFragment = MovieFragment()
                return images_fragment()
            }

            3 -> {
                return igtv_fragment()
            }

            4 -> {
                // val movieFragment = MovieFragment()
                return profile_image_fragment()
            }

            else -> getItem(position)
        }
    }
}
