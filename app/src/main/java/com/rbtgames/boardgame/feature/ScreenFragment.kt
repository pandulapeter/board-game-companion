package com.rbtgames.boardgame.feature

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.rbtgames.boardgame.BR

abstract class ScreenFragment<B : ViewDataBinding, VM : ScreenViewModel>(layoutResourceId: Int) : Fragment<B>(layoutResourceId) {

    protected abstract val viewModel: VM
    open val shouldUseTranslucentStatusBar = false

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        windowObserver?.setTranslucentStatusBar(shouldUseTranslucentStatusBar)
        windowObserver?.statusBarHeight?.let {
            if (it != 0) {
                applyWindowInsets(it)
            }
        }
        binding.setVariable(BR.viewModel, viewModel)
    }

    protected inline fun <T> LiveData<T>.observe(crossinline callback: (T) -> Unit) = observe(viewLifecycleOwner, Observer {
        callback(it)
    })
}