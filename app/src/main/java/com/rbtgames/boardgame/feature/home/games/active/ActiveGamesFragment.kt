package com.rbtgames.boardgame.feature.home.games.active

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentGamesActiveBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.feature.gameDetail.GameDetailFragment
import com.rbtgames.boardgame.feature.home.games.list.ActiveGameViewModel
import com.rbtgames.boardgame.feature.home.games.list.GameListAdapter
import com.rbtgames.boardgame.feature.shared.ElevationItemTouchHelperCallback
import com.rbtgames.boardgame.utils.dimension
import com.rbtgames.boardgame.utils.handleReplace
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActiveGamesFragment : ScreenFragment<FragmentGamesActiveBinding, ActiveGamesViewModel>(R.layout.fragment_games_active) {

    override val viewModel by viewModel<ActiveGamesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameListAdapter = GameListAdapter { game -> navigateToGameDetail(game.id) }
        val itemTouchHelper = ItemTouchHelper(object : ElevationItemTouchHelperCallback((context?.dimension(R.dimen.content_padding) ?: 0).toFloat()) {

            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) =
                when {
                    binding.recyclerView.isAnimating -> 0
                    viewModel.canSwipeItem(viewHolder.adapterPosition) && !viewModel.hasGameToDelete() -> makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
                    else -> 0
                }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewHolder.adapterPosition.also { position ->
                    if (position != RecyclerView.NO_POSITION) {
                        (gameListAdapter.getItem(position) as? ActiveGameViewModel?)?.game?.also { gameToDelete ->
                            viewModel.deleteGamePermanently()
                            showSnackbar(
                                message = getString(R.string.games_game_deleted_message),
                                actionResId = R.string.games_game_deleted_action,
                                action = { viewModel.cancelDeleteGame() },
                                dismissAction = { viewModel.deleteGamePermanently() }
                            )
                            viewModel.deleteGameTemporarily(gameToDelete.id)
                        }
                    }
                }
            }
        })
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = gameListAdapter
            itemTouchHelper.attachToRecyclerView(this)
        }
        viewModel.listItems.observe { listItems -> gameListAdapter.submitList(listItems) { viewModel.onLoadingDone() } }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshGames()
    }

    override fun applyWindowInsets(statusBarHeight: Int) = Unit

    private fun navigateToGameDetail(gameId: String) = activityFragmentManager?.handleReplace(addToBackStack = true) { GameDetailFragment.newInstance(gameId) }

    companion object {
        fun newInstance() = ActiveGamesFragment()
    }
}