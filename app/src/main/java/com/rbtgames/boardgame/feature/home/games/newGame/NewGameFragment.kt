package com.rbtgames.boardgame.feature.home.games.newGame

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.databinding.FragmentNewGameBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.feature.home.games.playerDetail.PlayerDetailFragment
import com.rbtgames.boardgame.utils.BundleArgumentDelegate
import com.rbtgames.boardgame.utils.handleReplace
import com.rbtgames.boardgame.utils.navigateBack
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NewGameFragment : ScreenFragment<FragmentNewGameBinding, NewGameViewModel>(R.layout.fragment_new_game) {

    override val viewModel by viewModel<NewGameViewModel> { parametersOf(arguments?.game ?: Game()) }
    override val transitionType = TransitionType.DETAIL
    private val playerAdapter = PlayerAdapter { playerViewModel -> navigateToNewPlayerScreen(playerViewModel.player) }
    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0) {
                binding.addPlayerButton.shrink()
                binding.startGameButton.shrink()
            } else {
                binding.addPlayerButton.extend()
                binding.startGameButton.extend()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = playerAdapter
            addOnScrollListener(onScrollListener)
        }
        viewModel.shouldNavigateBack.observeAndReset { parentFragmentManager?.navigateBack() }
        viewModel.shouldNavigateToNewPlayerScreen.observeAndReset { navigateToNewPlayerScreen(Player()) }
        viewModel.players.observe { players -> playerAdapter.submitList(players) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshPlayers()
    }

    override fun onDestroyView() {
        binding.recyclerView.removeOnScrollListener(onScrollListener)
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        arguments?.game = viewModel.game
    }

    private fun navigateToNewPlayerScreen(player: Player) =
        activityFragmentManager?.handleReplace(addToBackStack = true) { PlayerDetailFragment.newInstance(viewModel.game, player) }

    companion object {
        private var Bundle.game by BundleArgumentDelegate.Parcelable<Game>("game")

        fun newInstance() = NewGameFragment()
    }
}