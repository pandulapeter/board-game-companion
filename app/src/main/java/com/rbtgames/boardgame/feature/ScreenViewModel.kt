package com.rbtgames.boardgame.feature

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rbtgames.boardgame.feature.shared.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class ScreenViewModel : ViewModel(), CoroutineScope {

    private val job = Job()
    override val coroutineContext = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    protected fun <T> mutableLiveDataOf(initialValue: T) = MutableLiveData<T>().apply { value = initialValue }

    protected fun eventLiveData() = SingleLiveEvent<Boolean?>()

    protected fun MutableLiveData<Boolean?>.sendEvent() {
        value = true
    }
}