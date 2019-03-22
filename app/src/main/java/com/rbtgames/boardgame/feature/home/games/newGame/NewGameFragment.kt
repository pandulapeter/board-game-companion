package com.rbtgames.boardgame.feature.home.games.newGame

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.databinding.FragmentNewGameBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.feature.home.games.newGame.list.PlayerAdapter
import com.rbtgames.boardgame.feature.home.games.newGame.list.PlayerViewModel
import com.rbtgames.boardgame.feature.home.games.playerDetail.PlayerDetailFragment
import com.rbtgames.boardgame.feature.shared.AlertDialogFragment
import com.rbtgames.boardgame.feature.shared.ElevationItemTouchHelperCallback
import com.rbtgames.boardgame.utils.consume
import com.rbtgames.boardgame.utils.dimension
import com.rbtgames.boardgame.utils.handleReplace
import com.rbtgames.boardgame.utils.navigateBack
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewGameFragment : ScreenFragment<FragmentNewGameBinding, NewGameViewModel>(R.layout.fragment_new_game), AlertDialogFragment.OnDialogItemSelectedListener {

    override val viewModel by viewModel<NewGameViewModel>()
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
        val itemTouchHelper = ItemTouchHelper(object : ElevationItemTouchHelperCallback((context?.dimension(R.dimen.content_padding) ?: 0).toFloat()) {

            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) =
                when {
                    binding.recyclerView.isAnimating -> 0
                    viewModel.canSwipeItem(viewHolder.adapterPosition) -> makeMovementFlags(
                        if (viewModel.canMoveItem(viewHolder.adapterPosition)) ItemTouchHelper.UP or ItemTouchHelper.DOWN else 0,
                        if (viewModel.hasPlayerToDelete()) 0 else ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    )
                    else -> 0
                }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = consume {
                viewHolder.adapterPosition.let { originalPosition ->
                    target.adapterPosition.let { targetPosition ->
                        if (viewModel.canMoveItem(originalPosition) && viewModel.canMoveItem(targetPosition)) {
                            dismissSnackbar()
                            viewModel.swapPlayers(originalPosition, targetPosition)
                        }
                    }
                }
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewHolder.adapterPosition.let { position ->
                    if (position != RecyclerView.NO_POSITION) {
                        (playerAdapter?.getItem(position) as? PlayerViewModel?)?.player?.let { playerToDelete ->
                            showSnackbar(
                                message = getString(R.string.new_game_player_deleted_message, playerToDelete.name),
                                actionResId = R.string.new_game_player_deleted_action,
                                action = { viewModel.cancelDeletePlayer() },
                                dismissAction = { viewModel.deletePlayerPermanently() }
                            )
                            viewModel.deletePlayerTemporarily(playerToDelete.id)
                        }
                    }
                }
            }
        })
        playerAdapter?.dragHandleTouchListener = { position -> binding.recyclerView.findViewHolderForAdapterPosition(position)?.let { itemTouchHelper.startDrag(it) } }
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = playerAdapter
            itemTouchHelper.attachToRecyclerView(this)
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

    private fun navigateBack() = parentFragmentManager?.navigateBack()

    private fun navigateToNewPlayerScreen(player: Player) =
        activityFragmentManager?.handleReplace(addToBackStack = true) { PlayerDetailFragment.newInstance(player.id, viewModel.game.id) }

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

        fun newInstance() = NewGameFragment()
    }
}