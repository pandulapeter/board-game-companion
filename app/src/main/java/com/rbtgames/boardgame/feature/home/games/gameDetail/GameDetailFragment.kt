package com.rbtgames.boardgame.feature.home.games.gameDetail

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentGameDetailBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.feature.home.games.gameDetail.list.GameDetailAdapter
import com.rbtgames.boardgame.utils.BundleArgumentDelegate
import com.rbtgames.boardgame.utils.clearBackStack
import com.rbtgames.boardgame.utils.consume
import com.rbtgames.boardgame.utils.hideKeyboard
import com.rbtgames.boardgame.utils.showKeyboard
import com.rbtgames.boardgame.utils.withArguments
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class GameDetailFragment : ScreenFragment<FragmentGameDetailBinding, GameDetailViewModel>(R.layout.fragment_game_detail) {

    override val viewModel by viewModel<GameDetailViewModel> { parametersOf(arguments?.gameId) }
    override val transitionType = TransitionType.DETAIL
    private var gameDetailAdapter: GameDetailAdapter? = null
    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) = binding.nextTurnButton.run {
            if (dy > 0) {
                shrink()
            } else {
                extend()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pointsInput.apply {
            post { if (isAdded) showKeyboard(this) }
            setOnEditorActionListener { _, actionId, _ -> consume { if (actionId == EditorInfo.IME_ACTION_DONE) viewModel.onNextTurnButtonPressed() } }
        }
        gameDetailAdapter = GameDetailAdapter()
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = gameDetailAdapter
            addOnScrollListener(onScrollListener)
        }
        viewModel.shouldNavigateBack.observe { navigateBack() }
        viewModel.players.observe { playerViewModels -> gameDetailAdapter?.submitList(playerViewModels) }
    }

    private fun navigateBack() = parentFragmentManager?.clearBackStack()

    override fun onPause() {
        super.onPause()
        hideKeyboard(activity?.currentFocus)
    }

    override fun onBackPressed() = consume { navigateBack() }

    override fun onDestroyView() {
        binding.recyclerView.removeOnScrollListener(onScrollListener)
        gameDetailAdapter = null
        super.onDestroyView()
    }

    companion object {
        private var Bundle.gameId by BundleArgumentDelegate.String("gameId")

        fun newInstance(gameId: String) = GameDetailFragment().withArguments {
            it.gameId = gameId
        }
    }
}