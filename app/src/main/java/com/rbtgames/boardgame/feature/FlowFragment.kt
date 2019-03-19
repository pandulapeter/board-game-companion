package com.rbtgames.boardgame.feature

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.rbtgames.boardgame.R

abstract class FlowFragment<B : ViewDataBinding>(@LayoutRes layoutResourceId: Int) : Fragment<B>(layoutResourceId) {

    protected val currentFragment get() = childFragmentManager.findFragmentById(R.id.fragment_container) as? Fragment<*>

    override fun onBackPressed() = currentFragment.let {
        when {
            it == null -> false
            it.onBackPressed() -> true
            else -> childFragmentManager.popBackStackImmediate()
        }
    }
}