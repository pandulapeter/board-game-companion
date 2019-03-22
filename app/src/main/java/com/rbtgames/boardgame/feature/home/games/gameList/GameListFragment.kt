package com.rbtgames.boardgame.feature.home.games.gameList

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentGameListBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.feature.home.games.gameList.list.GameListAdapter
import com.rbtgames.boardgame.feature.home.games.newGame.NewGameFragment
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
        gameListAdapter = GameListAdapter { gameViewModel -> navigateToGame() }
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = gameListAdapter
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

    private fun navigateToGame() = showSnackbar(R.string.work_in_progress)

    companion object {
        fun newInstance() = GameListFragment()
    }
}