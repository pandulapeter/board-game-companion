package com.rbtgames.boardgame.feature.shared

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.rbtgames.boardgame.utils.BundleArgumentDelegate
import com.rbtgames.boardgame.utils.withArguments

class AlertDialogFragment : AppCompatDialogFragment() {

    private val onDialogItemSelectedListener get() = parentFragment as? OnDialogItemSelectedListener

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        (context?.let {
            AlertDialog.Builder(it)
                .setTitle(arguments.title)
                .setMessage(arguments.message)
                .setPositiveButton(arguments.positiveButton) { _, _ -> onDialogItemSelectedListener?.onPositiveButtonSelected(arguments.id) }
                .setNegativeButton(arguments.negativeButton) { _, _ -> onDialogItemSelectedListener?.onNegativeButtonSelected(arguments.id) }
                .create()
        } ?: super.onCreateDialog(savedInstanceState))

    interface OnDialogItemSelectedListener {

        fun onPositiveButtonSelected(id: Int)

        fun onNegativeButtonSelected(id: Int) = Unit
    }

    companion object {
        private var Bundle?.id by BundleArgumentDelegate.Int("id")
        private var Bundle?.title by BundleArgumentDelegate.Int("title")
        private var Bundle?.message by BundleArgumentDelegate.Int("message")
        private var Bundle?.positiveButton by BundleArgumentDelegate.Int("positiveButton")
        private var Bundle?.negativeButton by BundleArgumentDelegate.Int("negativeButton")

        fun show(id: Int, fragmentManager: FragmentManager, @StringRes title: Int, @StringRes message: Int, @StringRes positiveButton: Int, @StringRes negativeButton: Int) =
            AlertDialogFragment().withArguments {
                it.id = id
                it.title = title
                it.message = message
                it.positiveButton = positiveButton
                it.negativeButton = negativeButton
            }.run { show(fragmentManager, tag) }
    }
}