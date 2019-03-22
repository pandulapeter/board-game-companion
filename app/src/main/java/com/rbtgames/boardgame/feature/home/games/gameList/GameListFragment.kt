package com.rbtgames.boardgame.feature.home.games.gameList

import android.os.Bundle
import android.view.View
import com.google.android.material.appbar.AppBarLayout
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentGameListBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.feature.home.games.newGame.NewGameFragment
import com.rbtgames.boardgame.utils.handleReplace
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameListFragment : ScreenFragment<FragmentGameListBinding, GameListViewModel>(R.layout.fragment_game_list) {

    override val viewModel by viewModel<GameListViewModel>()
    private val onOffsetChangedListener = AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
        if (verticalOffset == 0) {
            binding.floatingActionButton.extend()
        } else {
            binding.floatingActionButton.shrink()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBarLayout.addOnOffsetChangedListener(onOffsetChangedListener)
        viewModel.shouldOpenNewGameScreen.observe { navigateToNewGame() }
    }

    override fun onDestroyView() {
        binding.appBarLayout.removeOnOffsetChangedListener(onOffsetChangedListener)
        super.onDestroyView()
    }

    private fun navigateToNewGame() = parentFragmentManager?.handleReplace(addToBackStack = true) { NewGameFragment.newInstance() }

    companion object {
        fun newInstance() = GameListFragment()
    }
}