package com.rbtgames.boardgame.feature.home.games.gameList

import android.os.Bundle
import android.transition.Slide
import android.view.View
import androidx.core.view.GravityCompat
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentGameListBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.feature.home.games.newGame.NewGameFragment
import com.rbtgames.boardgame.utils.handleReplace
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameListFragment : ScreenFragment<FragmentGameListBinding, GameListViewModel>(R.layout.fragment_game_list) {

    override val viewModel by viewModel<GameListViewModel>()
    override val initialExitTransition = Slide(GravityCompat.START)
    override val initialReenterTransition = Slide(GravityCompat.START)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.shouldOpenNewGameScreen.observeAndReset { navigateToNewGame() }
    }

    private fun navigateToNewGame() = parentFragmentManager?.handleReplace(addToBackStack = true) { NewGameFragment.newInstance() }

    companion object {
        fun newInstance() = GameListFragment()
    }
}