package com.rbtgames.boardgame.feature.home.games.gameList

import android.os.Bundle
import android.view.View
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentGameListBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.feature.home.games.newGame.NewGameFragment
import com.rbtgames.boardgame.utils.handleReplace
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameListFragment : ScreenFragment<FragmentGameListBinding, GameListViewModel>(R.layout.fragment_game_list) {

    override val viewModel by viewModel<GameListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.shouldOpenNewGameScreen.observeAndReset { navigateToNewGame() }
        //TODO: Remove this.
        var isExtended = true
        binding.text.setOnClickListener {
            if (isExtended) {
                binding.floatingActionButton.shrink()
            } else {
                binding.floatingActionButton.extend()
            }
            isExtended = !isExtended
        }
    }

    private fun navigateToNewGame() = parentFragmentManager?.handleReplace(addToBackStack = true) { NewGameFragment.newInstance() }

    companion object {
        fun newInstance() = GameListFragment()
    }
}