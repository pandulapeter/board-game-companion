package com.rbtgames.boardgame.feature.home.games.playerDetail

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentPlayerDetailBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.feature.home.games.playerDetail.list.ColorAdapter
import com.rbtgames.boardgame.feature.home.games.playerDetail.list.ColorItemDecoration
import com.rbtgames.boardgame.utils.BundleArgumentDelegate
import com.rbtgames.boardgame.utils.consume
import com.rbtgames.boardgame.utils.hideKeyboard
import com.rbtgames.boardgame.utils.navigateBack
import com.rbtgames.boardgame.utils.postDelayed
import com.rbtgames.boardgame.utils.showKeyboard
import com.rbtgames.boardgame.utils.withArguments
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerDetailFragment : ScreenFragment<FragmentPlayerDetailBinding, PlayerDetailViewModel>(R.layout.fragment_player_detail) {

    override val viewModel by viewModel<PlayerDetailViewModel> { parametersOf(arguments?.playerId, arguments?.gameId) }
    override val transitionType = TransitionType.MODAL
    private var colorAdapter: ColorAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        colorAdapter = ColorAdapter { color -> viewModel.onColorSelected(color) }
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
        viewModel.shouldNavigateBack.observe {
            val currentFocus = activity?.currentFocus
            if (currentFocus == null) {
                activityFragmentManager?.navigateBack()
            } else {
                hideKeyboard(currentFocus)
                binding.root.postDelayed(KEYBOARD_HIDE_DELAY) { if (isAdded) activityFragmentManager?.navigateBack() }
            }
        }
        viewModel.colors.observe { colorViewModels -> colorAdapter?.submitList(colorViewModels) }
        viewModel.initialSelectedColorIndex.observe { position -> binding.recyclerView.apply { postDelayed(KEYBOARD_HIDE_DELAY) { if (isAdded) scrollToPosition(position) } } }
    }

    override fun onDestroyView() {
        colorAdapter = null
        super.onDestroyView()
    }

    companion object {
        private const val KEYBOARD_HIDE_DELAY = 100L
        private var Bundle.playerId by BundleArgumentDelegate.String("playerId")
        private var Bundle.gameId by BundleArgumentDelegate.String("gameId")

        fun newInstance(playerId: String, gameId: String) = PlayerDetailFragment().withArguments {
            it.playerId = playerId
            it.gameId = gameId
        }
    }
}