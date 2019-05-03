package com.rbtgames.boardgame.feature.gameDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.data.model.Counter
import com.rbtgames.boardgame.databinding.FragmentCounterDialogBinding
import com.rbtgames.boardgame.utils.consume
import com.rbtgames.boardgame.utils.onTextChanged
import com.rbtgames.boardgame.utils.showKeyboard


class CounterDialogFragment : AppCompatDialogFragment() {

    private val onDialogItemSelectedListener get() = parentFragment as? OnDialogItemSelectedListener
    private val positiveButton by lazy { (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE) }
    private val binding by lazy { DataBindingUtil.inflate<FragmentCounterDialogBinding>(LayoutInflater.from(context), R.layout.fragment_counter_dialog, null, false) }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        (context?.let { context ->
            binding.nameEditText.onTextChanged { validate() }
            binding.pointsEditText.onTextChanged { validate() }
            binding.pointsEditText.setOnEditorActionListener { _, actionId, _ -> consume { if (actionId == EditorInfo.IME_ACTION_DONE) onAddButtonPressed() } }
            AlertDialog.Builder(context)
                .setTitle(R.string.game_detail_counter_new_counter)
                .setView(binding.root)
                .setPositiveButton(R.string.game_detail_counter_add) { _, _ -> onAddButtonPressed() }
                .setNegativeButton(R.string.cancel) { _, _ -> Unit }
                .create()
        } ?: super.onCreateDialog(savedInstanceState))

    override fun onResume() {
        super.onResume()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding.nameEditText.apply { post { showKeyboard(this) } }
        validate()
    }

    private fun onAddButtonPressed() {
        if (isInputValid()) {
            onDialogItemSelectedListener?.onCounterAdded(
                Counter(
                    name = binding.nameEditText.text.toString(),
                    points = try {
                        binding.pointsEditText.text.toString().toInt()
                    } catch (_: NumberFormatException) {
                        0
                    }
                )
            )
            dismiss()
        }
    }

    private fun validate() {
        positiveButton.isEnabled = isInputValid()
    }

    private fun isInputValid() = (binding.nameEditText.text ?: "").trim().isNotEmpty() && (binding.pointsEditText.text ?: "").trim().isNotEmpty()

    interface OnDialogItemSelectedListener {

        fun onCounterAdded(counter: Counter)
    }

    companion object {

        fun show(fragmentManager: FragmentManager) = CounterDialogFragment().run { show(fragmentManager, tag) }
    }
}