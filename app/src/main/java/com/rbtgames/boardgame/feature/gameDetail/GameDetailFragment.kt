package com.rbtgames.boardgame.feature.gameDetail

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.data.model.Counter
import com.rbtgames.boardgame.databinding.FragmentGameDetailBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import com.rbtgames.boardgame.feature.gameDetail.list.GameDetailAdapter
import com.rbtgames.boardgame.feature.shared.AlertDialogFragment
import com.rbtgames.boardgame.utils.BundleArgumentDelegate
import com.rbtgames.boardgame.utils.clearBackStack
import com.rbtgames.boardgame.utils.consume
import com.rbtgames.boardgame.utils.dimension
import com.rbtgames.boardgame.utils.hideKeyboard
import com.rbtgames.boardgame.utils.showKeyboard
import com.rbtgames.boardgame.utils.withArguments
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class GameDetailFragment : ScreenFragment<FragmentGameDetailBinding, GameDetailViewModel>(R.layout.fragment_game_detail), AlertDialogFragment.OnDialogItemSelectedListener,
    CounterDialogFragment.OnDialogItemSelectedListener {

    override val viewModel by viewModel<GameDetailViewModel> { parametersOf(arguments?.gameId) }
    override val transitionType = TransitionType.DETAIL
    override val shouldUseTranslucentStatusBar = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pointsInput.apply {
            setOnEditorActionListener { _, actionId, _ -> consume { if (actionId == EditorInfo.IME_ACTION_DONE) viewModel.onDoneButtonPressed() } }
        }
        val gameDetailAdapter = GameDetailAdapter()
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = gameDetailAdapter
            (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        }
        viewModel.shouldNavigateBack.observe { navigateBack() }
        viewModel.shouldShowOverflowMenu.observe { showOverflowMenu() }
        viewModel.players.observe { gameDetailListItems ->
            gameDetailAdapter.submitList(gameDetailListItems) {
                viewModel.onLoadingDone()
                binding.recyclerView.apply { post { smoothScrollToPosition(0) } }
            }
        }
        viewModel.shouldShowCounterDialog.observe { showCounterDialog() }
        viewModel.shouldShowFinishGameConfirmation.observe { showFinishGameConfirmation() }
        viewModel.isGameActive.observe {
            binding.root.postDelayed({
                if (isAdded) {
                    when (it) {
                        true -> showKeyboard(binding.pointsInput)
                        false -> hideKeyboard(activity?.currentFocus)
                    }
                }
            }, 100)
        }
    }

    private fun navigateBack() = activityFragmentManager?.clearBackStack()

    private fun showOverflowMenu() = PopupMenu(requireContext(), binding.moreButton, GravityCompat.END).apply {
        menu.apply {
            add(Menu.NONE, R.id.menu_undo, 0, R.string.game_detail_menu_undo)
            add(Menu.FIRST, R.id.menu_add_counter, 1, R.string.game_detail_menu_add_counter)
            add(2, R.id.menu_edit_players, 2, R.string.game_detail_menu_edit_players)
            add(Menu.FIRST, R.id.menu_finish_game, 3, R.string.game_detail_menu_finish_game)
            setGroupEnabled(Menu.NONE, viewModel.isUndoAvailable())
            setGroupEnabled(2, false)
            setOnMenuItemClickListener {
                dismiss()
                when (it.itemId) {
                    R.id.menu_undo -> consume { viewModel.onUndoButtonPressed() }
                    R.id.menu_add_counter -> consume { viewModel.onAddCounterButtonPressed() }
                    R.id.menu_edit_players -> consume { viewModel.onEditPlayersButtonPressed() }
                    R.id.menu_finish_game -> consume { viewModel.onFinishGameButtonPressed() }
                    else -> false
                }
            }
        }
    }.show()

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
            layoutParams = (layoutParams as LinearLayout.LayoutParams).apply {
                height = binding.container.height - binding.appBarLayout.height - keyboardHeight
            }
        }
    }

    override fun onPositiveButtonSelected(id: Int) {
        if (id == DIALOG_FINISH_GAME_CONFIRMATION_ID) {
            viewModel.finishGame()
        }
    }

    override fun onCounterAdded(counter: Counter) = viewModel.addCounter(counter)

    private fun showFinishGameConfirmation() = AlertDialogFragment.show(
        id = DIALOG_FINISH_GAME_CONFIRMATION_ID,
        fragmentManager = childFragmentManager,
        title = R.string.game_detail_finish_confirmation_title,
        message = R.string.game_detail_finish_confirmation_message,
        positiveButton = R.string.game_detail_finish_confirmation_positive,
        negativeButton = R.string.game_detail_finish_confirmation_negative
    )

    private fun showCounterDialog() = activityFragmentManager?.let { CounterDialogFragment.show(it) }

    companion object {
        private const val DIALOG_FINISH_GAME_CONFIRMATION_ID = 1

        private var Bundle.gameId by BundleArgumentDelegate.String("gameId")

        fun newInstance(gameId: String) = GameDetailFragment().withArguments {
            it.gameId = gameId
        }
    }
}