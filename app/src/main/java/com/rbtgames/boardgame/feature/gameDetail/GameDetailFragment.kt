package com.rbtgames.boardgame.feature.gameDetail

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentGameDetailBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.feature.gameDetail.list.GameDetailAdapter
import com.rbtgames.boardgame.utils.BundleArgumentDelegate
import com.rbtgames.boardgame.utils.clearBackStack
import com.rbtgames.boardgame.utils.consume
import com.rbtgames.boardgame.utils.dimension
import com.rbtgames.boardgame.utils.hideKeyboard
import com.rbtgames.boardgame.utils.postDelayed
import com.rbtgames.boardgame.utils.showKeyboard
import com.rbtgames.boardgame.utils.withArguments
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class GameDetailFragment : ScreenFragment<FragmentGameDetailBinding, GameDetailViewModel>(R.layout.fragment_game_detail) {

    override val viewModel by viewModel<GameDetailViewModel> { parametersOf(arguments?.gameId) }
    override val transitionType = TransitionType.DETAIL
    override val shouldUseTranslucentStatusBar = true
    private var gameDetailAdapter: GameDetailAdapter? = null
    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0) {
                hideKeyboard(activity?.currentFocus)
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
            (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
            addOnScrollListener(onScrollListener)
        }
        viewModel.shouldNavigateBack.observe { navigateBack() }
        viewModel.shouldShowOverflowMenu.observe { showOverflowMenu() }
        viewModel.players.observe { playerViewModels ->
            gameDetailAdapter?.submitList(playerViewModels)
            binding.recyclerView.apply { postDelayed(SCROLL_TO_TOP_DELAY) { if (isAdded) smoothScrollToPosition(0) } }
        }
    }

    private fun navigateBack() = activityFragmentManager?.clearBackStack()

    private fun showOverflowMenu() {
        PopupMenu(requireContext(), binding.moreButton, GravityCompat.END).apply {
            menu.apply {
                add("Finish game")
                add("Add counter")
            }
            show()
        }
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard(activity?.currentFocus)
    }

    override fun onBackPressed() = consume { navigateBack() }

    override fun applyWindowInsets(statusBarHeight: Int) {
        binding.backButton.apply {
            layoutParams = (layoutParams as ConstraintLayout.LayoutParams).apply {
                topMargin = context.dimension(R.dimen.first_keyline_button) + statusBarHeight
            }
        }
    }

    override fun onKeyboardHeightChanged(keyboardHeight: Int) {
        binding.listContainer.apply {
            post {
                if (isAdded) {
                    layoutParams = (layoutParams as LinearLayout.LayoutParams).apply {
                        height = binding.container.height - binding.appBarLayout.height - keyboardHeight
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.recyclerView.removeOnScrollListener(onScrollListener)
        gameDetailAdapter = null
        super.onDestroyView()
    }

    companion object {
        private const val SCROLL_TO_TOP_DELAY = 100L
        private var Bundle.gameId by BundleArgumentDelegate.String("gameId")

        fun newInstance(gameId: String) = GameDetailFragment().withArguments {
            it.gameId = gameId
        }
    }
}