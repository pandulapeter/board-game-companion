package com.rbtgames.boardgame.feature

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeTransform
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class Fragment<B : ViewDataBinding>(@LayoutRes private val layoutResourceId: Int) : Fragment() {

    private var realBinding: B? = null
    protected val binding get() = realBinding ?: throw IllegalStateException("Trying to access a null binding.")
    protected open val initialEnterTransition: Transition? = Fade()
    protected open val initialReenterTransition: Transition? = null
    protected open val initialExitTransition: Transition? = Fade()
    protected open val initialReturnTransition: Transition? get() = initialExitTransition
    protected val activityFragmentManager get() = (activity as? AppCompatActivity?)?.supportFragmentManager
    protected val parentFragmentManager get() = parentFragment?.childFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allowEnterTransitionOverlap = true
        allowReturnTransitionOverlap = true
        resetTransitions()
        sharedElementEnterTransition = TransitionSet().apply {
            addTransition(ChangeTransform())
            addTransition(ChangeBounds())
        }
        sharedElementReturnTransition = sharedElementEnterTransition
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
        super.onPause()
        resetTransitions()
    }

    open fun onBackPressed() = false

    private fun resetTransitions() {
        enterTransition = initialEnterTransition
        reenterTransition = initialReenterTransition
        exitTransition = initialExitTransition
        returnTransition = initialReturnTransition
    }
}