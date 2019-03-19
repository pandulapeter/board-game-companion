package com.rbtgames.boardgame.feature.home.games

import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentGamesBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class GamesFragment : ScreenFragment<FragmentGamesBinding, GamesViewModel>(R.layout.fragment_games) {

    override val viewModel by viewModel<GamesViewModel>()

    companion object {
        fun newInstance() = GamesFragment()
    }
}