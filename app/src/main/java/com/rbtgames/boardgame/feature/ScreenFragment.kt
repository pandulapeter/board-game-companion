package com.rbtgames.boardgame.feature

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.rbtgames.boardgame.BR

abstract class ScreenFragment<B : ViewDataBinding, VM : ScreenViewModel>(layoutResourceId: Int) : Fragment<B>(layoutResourceId) {

    protected abstract val viewModel: VM

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)
    }

    protected inline fun <T> LiveData<T>.observe(crossinline callback: (T) -> Unit) = observe(viewLifecycleOwner, Observer {
        callback(it)
    })

    protected inline fun <T> LiveData<T>.observeNonNull(crossinline observer: (T) -> Unit) {
        observe(viewLifecycleOwner, androidx.lifecycle.Observer { data ->
            data?.let { observer(it) }
        })
    }

    protected inline fun <T> MutableLiveData<T?>.observeAndReset(crossinline callback: (T) -> Unit) = observe(viewLifecycleOwner, Observer {
        if (it != null) {
            callback(it)
            value = null
        }
    })
}