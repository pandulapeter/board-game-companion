package com.rbtgames.boardgame.feature.home.games.newGame

import android.os.Bundle
import android.transition.Slide
import android.view.View
import androidx.core.view.GravityCompat
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentNewGameBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.feature.home.games.newGame.newPlayer.NewPlayerFragment
import com.rbtgames.boardgame.utils.handleReplace
import com.rbtgames.boardgame.utils.navigateBack
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewGameFragment : ScreenFragment<FragmentNewGameBinding, NewGameViewModel>(R.layout.fragment_new_game) {

    override val viewModel by viewModel<NewGameViewModel>()
    override val initialEnterTransition = Slide(GravityCompat.END)
    override val initialReturnTransition = Slide(GravityCompat.END)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.shouldNavigateBack.observeAndReset { parentFragmentManager?.navigateBack() }
        viewModel.shouldNavigateToNewPlayerScreen.observeAndReset { navigateToNewPlayerScreen() }
        //TODO: Remove this.
        var isExtended = true
        binding.text.setOnClickListener {
            if (isExtended) {
                binding.addPlayerButton.shrink()
                binding.startGameButton.shrink()
            } else {
                binding.addPlayerButton.extend()
                binding.startGameButton.extend()
            }
            isExtended = !isExtended
        }
    }

    private fun navigateToNewPlayerScreen() = activityFragmentManager?.handleReplace(addToBackStack = true) { NewPlayerFragment.newInstance() }

    companion object {
        fun newInstance() = NewGameFragment()
    }
}