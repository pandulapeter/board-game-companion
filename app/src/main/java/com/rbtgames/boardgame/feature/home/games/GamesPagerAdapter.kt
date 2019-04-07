package com.rbtgames.boardgame.feature.home.games

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.feature.home.games.active.GameListFragment

class GamesPagerAdapter(private val context: Context, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int) = GameListFragment.newInstance() //TODO

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> context.getString(R.string.game_list_active)
        1 -> context.getString(R.string.game_list_finished)
        else -> ""
    }

    override fun getCount() = 2
}