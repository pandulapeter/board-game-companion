package com.rbtgames.boardgame.feature.home.games.newGame

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.databinding.FragmentNewGameBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.feature.home.games.newGame.list.PlayerAdapter
import com.rbtgames.boardgame.feature.home.games.playerDetail.PlayerDetailFragment
import com.rbtgames.boardgame.feature.shared.AlertDialogFragment
import com.rbtgames.boardgame.utils.BundleArgumentDelegate
import com.rbtgames.boardgame.utils.consume
import com.rbtgames.boardgame.utils.handleReplace
import com.rbtgames.boardgame.utils.navigateBack
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NewGameFragment : ScreenFragment<FragmentNewGameBinding, NewGameViewModel>(R.layout.fragment_new_game), AlertDialogFragment.OnDialogItemSelectedListener {

    override val viewModel by viewModel<NewGameViewModel> { parametersOf(arguments?.game ?: Game()) }
    override val transitionType = TransitionType.DETAIL
    private var playerAdapter: PlayerAdapter? = null
    private val onOffsetChangedListener = AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
        if (verticalOffset == 0) {
            binding.addPlayerButton.extend()
            binding.startGameButton.extend()
        } else {
            binding.addPlayerButton.shrink()
            binding.startGameButton.shrink()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerAdapter = PlayerAdapter { playerViewModel -> navigateToNewPlayerScreen(playerViewModel.player) }
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = playerAdapter
        }
        binding.appBarLayout.addOnOffsetChangedListener(onOffsetChangedListener)
        viewModel.shouldShowCloseConfirmation.observe { showCloseConfirmationDialog() }
        viewModel.shouldNavigateBack.observe { navigateBack() }
        viewModel.shouldNavigateToNewPlayerScreen.observe { navigateToNewPlayerScreen(Player()) }
        viewModel.players.observe { players -> playerAdapter?.submitList(players) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshPlayers()
    }

    override fun onBackPressed() = consume { viewModel.onBackButtonPressed() }

    override fun onPositiveButtonSelected(id: Int) {
        if (id == DIALOG_CLOSE_CONFIRMATION_ID) {
            navigateBack()
        }
    }

    override fun onDestroyView() {
        playerAdapter = null
        binding.appBarLayout.removeOnOffsetChangedListener(onOffsetChangedListener)
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        arguments?.game = viewModel.game
    }

    private fun navigateBack() = parentFragmentManager?.navigateBack()

    private fun navigateToNewPlayerScreen(player: Player) =
        activityFragmentManager?.handleReplace(addToBackStack = true) { PlayerDetailFragment.newInstance(viewModel.game, player) }

    private fun showCloseConfirmationDialog() = AlertDialogFragment.show(
        id = DIALOG_CLOSE_CONFIRMATION_ID,
        fragmentManager = childFragmentManager,
        title = R.string.new_game_close_confirmation_title,
        message = R.string.new_game_close_confirmation_message,
        positiveButton = R.string.new_game_close_confirmation_positive,
        negativeButton = R.string.new_game_close_confirmation_negative
    )

    companion object {
        private const val DIALOG_CLOSE_CONFIRMATION_ID = 1
        private var Bundle.game by BundleArgumentDelegate.Parcelable<Game>("game")

        fun newInstance() = NewGameFragment()
    }
}