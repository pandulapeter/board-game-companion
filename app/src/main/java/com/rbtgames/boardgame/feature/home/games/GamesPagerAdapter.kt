package com.rbtgames.boardgame.feature.home.games

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.feature.home.games.active.ActiveGamesFragment
import com.rbtgames.boardgame.feature.home.games.finished.FinishedGamesFragment

class GamesPagerAdapter(private val context: Context, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int) = when (position) {
        0 -> ActiveGamesFragment.newInstance()
        1 -> FinishedGamesFragment.newInstance()
        else -> throw IllegalArgumentException("No BaseFragment defined for position $position.")
    }

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> context.getString(R.string.games_active)
        1 -> context.getString(R.string.games_finished)
        else -> ""
    }

    override fun getCount() = 2
}