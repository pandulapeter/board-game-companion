package com.rbtgames.boardgame.feature.home.games.playerDetail

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.databinding.FragmentPlayerDetailBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.utils.BundleArgumentDelegate
import com.rbtgames.boardgame.utils.consume
import com.rbtgames.boardgame.utils.hideKeyboard
import com.rbtgames.boardgame.utils.navigateBack
import com.rbtgames.boardgame.utils.showKeyboard
import com.rbtgames.boardgame.utils.withArguments
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerDetailFragment : ScreenFragment<FragmentPlayerDetailBinding, PlayerDetailViewModel>(R.layout.fragment_player_detail) {

    override val viewModel by viewModel<PlayerDetailViewModel> { parametersOf(arguments?.game, arguments?.player) }
    override val transitionType = TransitionType.MODAL
    private val colorAdapter = ColorAdapter { color -> viewModel.onColorSelected(color) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = colorAdapter
            addItemDecoration(ColorItemDecoration(requireContext()))
        }
        binding.playerNameInput.apply {
            post { if (isAdded) showKeyboard(this) }
            setOnEditorActionListener { _, actionId, _ -> consume { if (actionId == EditorInfo.IME_ACTION_DONE) viewModel.onDoneButtonPressed() } }
        }
        viewModel.shouldNavigateBack.observeAndReset {
            val currentFocus = activity?.currentFocus
            if (currentFocus == null) {
                activityFragmentManager?.navigateBack()
            } else {
                hideKeyboard(currentFocus)
                binding.root.postDelayed({ if (isAdded) activityFragmentManager?.navigateBack() }, 100)
            }
        }
        viewModel.colors.observe { colorViewModels -> colorAdapter.submitList(colorViewModels) }
    }

    companion object {
        private var Bundle.game by BundleArgumentDelegate.Parcelable<Game>("game")
        private var Bundle.player by BundleArgumentDelegate.Parcelable<Player>("player")

        fun newInstance(game: Game, player: Player) = PlayerDetailFragment().withArguments {
            it.game = game
            it.player = player
        }
    }
}