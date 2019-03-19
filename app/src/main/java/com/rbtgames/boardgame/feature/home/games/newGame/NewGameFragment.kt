package com.rbtgames.boardgame.feature.home.games.newGame

import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.View
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentNewGameBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.utils.navigateBack
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewGameFragment : ScreenFragment<FragmentNewGameBinding, NewGameViewModel>(R.layout.fragment_new_game) {

    override val viewModel by viewModel<NewGameViewModel>()
    override val initialEnterTransition = Slide(Gravity.BOTTOM)
    override val initialReturnTransition = Slide(Gravity.BOTTOM)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.shouldNavigateBack.observeAndReset { parentFragmentManager?.navigateBack() }
    }

    companion object {
        fun newInstance() = NewGameFragment()
    }
}