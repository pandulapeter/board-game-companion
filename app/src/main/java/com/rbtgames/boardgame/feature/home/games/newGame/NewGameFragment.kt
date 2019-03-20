package com.rbtgames.boardgame.feature.home.games.newGame

import android.os.Bundle
import android.view.View
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.databinding.FragmentNewGameBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.feature.home.games.playerDetail.PlayerDetailFragment
import com.rbtgames.boardgame.utils.handleReplace
import com.rbtgames.boardgame.utils.navigateBack
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewGameFragment : ScreenFragment<FragmentNewGameBinding, NewGameViewModel>(R.layout.fragment_new_game) {

    override val viewModel by viewModel<NewGameViewModel>()
    override val transitionType = TransitionType.DETAIL

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.shouldNavigateBack.observeAndReset { parentFragmentManager?.navigateBack() }
        viewModel.shouldNavigateToNewPlayerScreen.observeAndReset { navigateToNewPlayerScreen() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshPlayers()
    }

    private fun navigateToNewPlayerScreen() = activityFragmentManager?.handleReplace(addToBackStack = true) { PlayerDetailFragment.newInstance(viewModel.game, Player()) }

    companion object {
        fun newInstance() = NewGameFragment()
    }
}