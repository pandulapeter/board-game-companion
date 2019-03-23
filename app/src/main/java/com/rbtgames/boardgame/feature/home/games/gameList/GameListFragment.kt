package com.rbtgames.boardgame.feature.home.games.gameList

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentGameListBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.feature.home.games.gameDetail.GameDetailFragment
import com.rbtgames.boardgame.feature.home.games.gameList.list.GameListAdapter
import com.rbtgames.boardgame.feature.home.games.gameList.list.GameViewModel
import com.rbtgames.boardgame.feature.home.games.newGame.NewGameFragment
import com.rbtgames.boardgame.feature.shared.ElevationItemTouchHelperCallback
import com.rbtgames.boardgame.utils.dimension
import com.rbtgames.boardgame.utils.handleReplace
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameListFragment : ScreenFragment<FragmentGameListBinding, GameListViewModel>(R.layout.fragment_game_list) {

    override val viewModel by viewModel<GameListViewModel>()
    private var gameListAdapter: GameListAdapter? = null
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
        gameListAdapter = GameListAdapter { gameViewModel -> navigateToGameDetail(gameViewModel.game.id) }
        val itemTouchHelper = ItemTouchHelper(object : ElevationItemTouchHelperCallback((context?.dimension(R.dimen.content_padding) ?: 0).toFloat()) {

            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) =
                when {
                    binding.recyclerView.isAnimating -> 0
                    viewModel.canSwipeItem(viewHolder.adapterPosition) -> makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
                    else -> 0
                }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewHolder.adapterPosition.also { position ->
                    if (position != RecyclerView.NO_POSITION) {
                        (gameListAdapter?.getItem(position) as? GameViewModel?)?.game?.also { gameToDelete ->
                            viewModel.deleteGamePermanently()
                            showSnackbar(
                                message = getString(R.string.game_list_game_deleted_message),
                                actionResId = R.string.game_list_game_deleted_action,
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
        viewModel.shouldOpenNewGameScreen.observe { navigateToNewGame() }
        viewModel.listItems.observe { listItems -> gameListAdapter?.submitList(listItems) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshGames()
    }

    override fun onDestroyView() {
        gameListAdapter = null
        binding.appBarLayout.removeOnOffsetChangedListener(onOffsetChangedListener)
        super.onDestroyView()
    }

    private fun navigateToNewGame() = parentFragmentManager?.handleReplace(addToBackStack = true) { NewGameFragment.newInstance() }

    private fun navigateToGameDetail(gameId: String) = parentFragmentManager?.handleReplace(addToBackStack = true) { GameDetailFragment.newInstance(gameId) }

    companion object {
        fun newInstance() = GameListFragment()
    }
}