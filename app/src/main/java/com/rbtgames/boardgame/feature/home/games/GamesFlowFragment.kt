package com.rbtgames.boardgame.feature.home.games

import android.os.Bundle
import android.view.View
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentGamesFlowBinding
import com.rbtgames.boardgame.feature.FlowFragment
import com.rbtgames.boardgame.feature.home.games.gameList.GameListFragment
import com.rbtgames.boardgame.utils.handleReplace

class GamesFlowFragment : FlowFragment<FragmentGamesFlowBinding>(R.layout.fragment_games_flow) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null && currentFragment == null) {
            childFragmentManager.handleReplace { GameListFragment.newInstance() }
        }
    }

    companion object {
        fun newInstance() = GamesFlowFragment()
    }
}