package com.rbtgames.boardgame.feature.home.games.newGame.newPlayer

import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.View
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentNewPlayerBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.utils.navigateBack
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlayerFragment : ScreenFragment<FragmentNewPlayerBinding, NewPlayerViewModel>(R.layout.fragment_new_player) {

    override val viewModel by viewModel<NewPlayerViewModel>()
    override val initialEnterTransition = Slide(Gravity.BOTTOM)
    override val initialReturnTransition = Slide(Gravity.BOTTOM)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.shouldNavigateBack.observeAndReset { activityFragmentManager?.navigateBack() }
    }

    companion object {
        fun newInstance() = NewPlayerFragment()
    }
}