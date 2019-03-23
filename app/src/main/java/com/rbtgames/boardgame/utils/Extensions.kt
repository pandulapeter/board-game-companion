package com.rbtgames.boardgame.utils

import android.content.Context
import android.os.Bundle
import android.view.View
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