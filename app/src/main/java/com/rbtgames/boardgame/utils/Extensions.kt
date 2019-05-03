package com.rbtgames.boardgame.utils

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.rbtgames.boardgame.R

fun Context.color(@ColorRes colorResInt: Int) = ContextCompat.getColor(this, colorResInt)

fun Context.dimension(@DimenRes dimensionResInt: Int) = resources.getDimensionPixelOffset(dimensionResInt)

fun Context.drawable(@DrawableRes drawableResInt: Int) = AppCompatResources.getDrawable(this, drawableResInt)

var View.visible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

fun View.postDelayed(delay: Long, action: () -> Unit) = postDelayed(action, delay)

fun View.animateCircularReveal(onAnimationEnd: () -> Unit) {
    if (isAttachedToWindow) {
        val cx = Math.round(width * 0.8).toInt()
        val cy = Math.round(height * 0.8).toInt()
        val maxRadius = Math.hypot(width.toDouble(), height.toDouble()).toFloat()
        visible = true
        val animator = ViewAnimationUtils.createCircularReveal(this, cx, cy, 0f, maxRadius).apply {
            addListener(
                onAnimationEnd = {
                    onAnimationEnd()
                    removeAllListeners()
                },
                onAnimationCancel = {
                    onAnimationEnd()
                    removeAllListeners()
                })
        }
        animator.start()
    }
}

inline fun EditText.onTextChanged(crossinline callback: (String) -> Unit) = addTextChangedListener(object : TextWatcher {

    override fun afterTextChanged(s: Editable?) = Unit

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = callback(s?.toString() ?: "")
})

inline fun Animator.addListener(
    crossinline onAnimationRepeat: () -> Unit = {},
    crossinline onAnimationEnd: () -> Unit = {},
    crossinline onAnimationCancel: () -> Unit = {},
    crossinline onAnimationStart: () -> Unit = {}
) = addListener(object : Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator?) = onAnimationRepeat()

    override fun onAnimationEnd(animation: Animator?) = onAnimationEnd()

    override fun onAnimationCancel(animation: Animator?) = onAnimationCancel()

    override fun onAnimationStart(animation: Animator?) = onAnimationStart()
})

inline fun <reified T : Fragment> FragmentManager.handleReplace(
    vararg sharedViews: View?,
    tag: String = T::class.java.name,
    addToBackStack: Boolean = false,
    @IdRes containerId: Int = R.id.fragment_container,
    crossinline newInstance: () -> T
) {
    beginTransaction().apply {
        sharedViews.filterNotNull().fold(this) { transaction, sharedElement ->
            transaction.addSharedElement(sharedElement, ViewCompat.getTransitionName(sharedElement).orEmpty())
        }
        replace(containerId, findFragmentByTag(tag) ?: newInstance.invoke(), tag)
        if (addToBackStack) {
            addToBackStack(null)
        }
        setReorderingAllowed(true)
        commitAllowingStateLoss()
    }
}

fun FragmentManager.navigateBack() = popBackStackImmediate()

fun FragmentManager.clearBackStack() = repeat(backStackEntryCount) { popBackStack() }

inline fun <T : Fragment> T.withArguments(bundleOperations: (Bundle) -> Unit): T = apply {
    arguments = Bundle().apply { bundleOperations(this) }
}