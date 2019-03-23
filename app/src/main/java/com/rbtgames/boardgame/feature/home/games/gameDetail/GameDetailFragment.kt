package com.rbtgames.boardgame.feature.home.games.gameDetail

import android.os.Bundle
import android.view.View
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentGameDetailBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.utils.BundleArgumentDelegate
import com.rbtgames.boardgame.utils.clearBackStack
import com.rbtgames.boardgame.utils.consume
import com.rbtgames.boardgame.utils.withArguments
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class GameDetailFragment : ScreenFragment<FragmentGameDetailBinding, GameDetailViewModel>(R.layout.fragment_game_detail) {

    override val viewModel by viewModel<GameDetailViewModel> { parametersOf(arguments?.gameId) }
    override val transitionType = TransitionType.DETAIL

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.shouldNavigateBack.observe { navigateBack() }
    }

    private fun navigateBack() = parentFragmentManager?.clearBackStack()

    override fun onBackPressed() = consume { navigateBack() }

    companion object {
        private var Bundle.gameId by BundleArgumentDelegate.String("gameId")

        fun newInstance(gameId: String) = GameDetailFragment().withArguments {
            it.gameId = gameId
        }
    }
}