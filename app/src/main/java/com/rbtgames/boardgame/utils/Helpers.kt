package com.rbtgames.boardgame.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.MutableLiveData

inline fun consume(crossinline action: () -> Unit) = true.also { action() }

fun <T> mutableLiveDataOf(initialValue: T) = MutableLiveData<T>().apply { value = initialValue }

fun eventLiveData() = MutableLiveData<Boolean?>()

fun showKeyboard(focusedView: View?) = focusedView?.also {
    it.requestFocus()
    (it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(it, 0)
}

fun hideKeyboard(focusedView: View?) = focusedView?.also {
    it.clearFocus()
    (it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(it.windowToken, 0)
}