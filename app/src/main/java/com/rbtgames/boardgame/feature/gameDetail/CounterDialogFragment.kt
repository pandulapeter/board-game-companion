package com.rbtgames.boardgame.feature.gameDetail

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.data.model.Counter

class CounterDialogFragment : AppCompatDialogFragment() {

    private val onDialogItemSelectedListener get() = parentFragment as? OnDialogItemSelectedListener

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        (context?.let {
            AlertDialog.Builder(it)
                //TODO
                .setTitle("New counter")
                .setPositiveButton("Add") { _, _ ->
                    onDialogItemSelectedListener?.onCounterAdded(
                        Counter(
                            name = "new counter ${(0..100).random()}",
                            points = (0..100).random()
                        )
                    )
                }
                .setNegativeButton(R.string.cancel) { _, _ -> Unit }
                .create()
        } ?: super.onCreateDialog(savedInstanceState))

    interface OnDialogItemSelectedListener {

        fun onCounterAdded(counter: Counter)
    }

    companion object {

        fun show(fragmentManager: FragmentManager) = CounterDialogFragment().run { show(fragmentManager, tag) }
    }
}