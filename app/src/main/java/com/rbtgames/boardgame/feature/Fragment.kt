package com.rbtgames.boardgame.feature

import android.content.res.Resources
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.rbtgames.boardgame.WindowObserver
import com.rbtgames.boardgame.feature.shared.KeyboardHeightProvider

abstract class Fragment<B : ViewDataBinding>(@LayoutRes private val layoutResourceId: Int) : Fragment() {

    private var realBinding: B? = null
    protected val binding get() = realBinding ?: throw IllegalStateException("Trying to access a null binding.")
    protected val activityFragmentManager get() = (activity as? AppCompatActivity?)?.supportFragmentManager
    protected val parentFragmentManager get() = parentFragment?.childFragmentManager
    protected open val transitionType = TransitionType.SIBLING
    protected val windowObserver get() = activity as? WindowObserver?
    private var snackbar: Snackbar? = null
    private var keyboardHeightProvider: KeyboardHeightProvider? = null
    private var previousKeyboardHeight = 0
    private val keyboardListener = object : KeyboardHeightProvider.KeyboardListener {
        override fun onHeightChanged(height: Int) {
            if (previousKeyboardHeight != height) {
                windowObserver?.keyboardHeight = height
                previousKeyboardHeight = height
                onKeyboardHeightChanged(height)
            }
        }
    }

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

    override fun onResume() {
        super.onResume()
        keyboardHeightProvider = KeyboardHeightProvider(requireActivity()).apply {
            addKeyboardListener(keyboardListener)
        }
        binding.root.post {
            keyboardListener.onHeightChanged((activity as? WindowObserver?)?.keyboardHeight ?: 0)
            keyboardHeightProvider?.start()
        }
    }

    override fun onPause() {
        dismissSnackbar()
        super.onPause()
        resetTransitions()
        keyboardHeightProvider?.apply {
            removeKeyboardListener(keyboardListener)
            close()
        }
        keyboardHeightProvider = null
        previousKeyboardHeight = 0
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        realBinding = null
    }

    private fun dismissSnackbar() {
        snackbar?.dismiss()
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        val parent = parentFragment
        return if (!enter && parent != null && parent.isRemoving) {
            // This is a workaround for the bug where child fragments disappear when
            // the parent is removed (as all children are first removed from the parent)
            // See https://code.google.com/p/android/issues/detail?id=55228
            AlphaAnimation(1f, 1f).apply { duration = getNextAnimationDuration(parent, 300L) }
        } else {
            super.onCreateAnimation(transit, enter, nextAnim)
        }
    }

    open fun applyWindowInsets(statusBarHeight: Int) = binding.root.run { setPadding(paddingStart, statusBarHeight, paddingEnd, paddingBottom) }

    protected open fun onKeyboardHeightChanged(keyboardHeight: Int) = Unit

    open fun onBackPressed() = false

    protected fun showSnackbar(message: String, @StringRes actionResId: Int = 0, action: (() -> Unit)? = null, dismissAction: (() -> Unit)? = null) {
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

    private fun getNextAnimationDuration(fragment: Fragment, defValue: Long): Long {
        try {
            val animInfoField = Fragment::class.java.getDeclaredField("mAnimationInfo")
            animInfoField.isAccessible = true
            val animationInfo = animInfoField.get(fragment)
            val nextAnimField = animationInfo.javaClass.getDeclaredField("mNextAnim")
            nextAnimField.isAccessible = true
            val nextAnimResource = nextAnimField.getInt(animationInfo)
            val nextAnim = AnimationUtils.loadAnimation(fragment.activity, nextAnimResource)
            return nextAnim?.duration ?: defValue
        } catch (ex: NoSuchFieldException) {
            return defValue
        } catch (ex: IllegalAccessException) {
            return defValue
        } catch (ex: Resources.NotFoundException) {
            return defValue
        }
    }

    protected enum class TransitionType {
        SIBLING, DETAIL, MODAL
    }
}