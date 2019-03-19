package com.rbtgames.boardgame.utils

import androidx.lifecycle.MutableLiveData

inline fun consume(crossinline action: () -> Unit) = true.also { action() }

fun <T> mutableLiveDataOf(initialValue: T) = MutableLiveData<T>().apply { value = initialValue }

fun eventLiveData() = MutableLiveData<Boolean?>()