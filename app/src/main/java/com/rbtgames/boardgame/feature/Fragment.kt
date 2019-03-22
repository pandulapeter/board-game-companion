package com.rbtgames.boardgame.feature

import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

abstract class Fragment<B : ViewDataBinding>(@LayoutRes private val layoutResourceId: Int) : Fragment() {

    private var realBinding: B? = null
    protected val binding get() = realBinding ?: throw IllegalStateException("Trying to access a null binding.")
    protected val activityFragmentManager get() = (activity as? AppCompatActivity?)?.supportFragmentManager
    protected val parentFragmentManager get() = parentFragment?.childFragmentManager
    protected open val transitionType = TransitionType.SIBLING
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allowEnterTransitionOverlap = true
        allowReturnTransitionOverlap = true
        resetTransitions()
    }

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        DataBindingUtil.inflate<B>(inflater, layoutResourceId, container, false).also {
            realBinding = it
        }.root

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        realBinding = null
    }

    override fun onPause() {
        dismissSnackbar()
        super.onPause()
        resetTransitions()
    }

    protected fun dismissSnackbar() {
        snackbar?.dismiss()
    }

    open fun onBackPressed() = false

    protected fun showSnackbar(message: String, @StringRes actionResId: Int = 0, action: (() -> Unit)? = null, dismissAction: (() -> Unit)? = null) {
        dismissSnackbar()
        snackbar = Snackbar.make(binding.root, message, if (action == null && dismissAction == null) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_LONG).apply {
            if (actionResId != 0 && action != null) {
                setAction(actionResId) { action() }
            }
            addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (event != DISMISS_EVENT_ACTION && event != DISMISS_EVENT_CONSECUTIVE) {
                        dismissAction?.invoke()
                    }
                    removeCallback(this)
                    snackbar = null
                }
            })
        }.apply { show() }
    }

    protected fun showSnackbar(@StringRes messageResId: Int, @StringRes actionResId: Int = 0, action: (() -> Unit)? = null, dismissAction: (() -> Unit)? = null) =
        showSnackbar(requireContext().getString(messageResId), actionResId, action, dismissAction)


    private fun resetTransitions() {
        reenterTransition = Fade()
        exitTransition = Fade()
        enterTransition = when (transitionType) {
            TransitionType.SIBLING -> Fade()
            TransitionType.DETAIL -> Slide(GravityCompat.getAbsoluteGravity(GravityCompat.END, resources.configuration.layoutDirection))
            TransitionType.MODAL -> Slide(Gravity.BOTTOM)
        }
        returnTransition = enterTransition
    }

    protected enum class TransitionType {
        SIBLING, DETAIL, MODAL
    }
}