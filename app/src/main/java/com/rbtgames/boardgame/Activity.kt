package com.rbtgames.boardgame

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.rbtgames.boardgame.databinding.ActivityBinding
import com.rbtgames.boardgame.feature.Fragment
import com.rbtgames.boardgame.feature.home.HomeFlowFragment
import com.rbtgames.boardgame.utils.handleReplace

class Activity : AppCompatActivity(), WindowObserver {
    private lateinit var binding: ActivityBinding
    private var isStatusBarTranslucent = false
    override val statusBarHeight get() = if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isInMultiWindowMode)) 1 else binding.fragmentContainer.statusBarHeight
    override var keyboardHeight = 0

    private val currentFragment get() = supportFragmentManager.findFragmentById(R.id.fragment_container) as? Fragment<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity)
        binding.fragmentContainer.windowInsetChangeListener = {
            updateStatusBar()
            currentFragment?.applyWindowInsets(statusBarHeight)
        }
        if (savedInstanceState == null) {
            supportFragmentManager.handleReplace { HomeFlowFragment.newInstance() }
        }
    }

    override fun onBackPressed() {
        if (currentFragment?.onBackPressed() != true) {
            super.onBackPressed()
        }
    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean, newConfig: Configuration?) {
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig)
        updateStatusBar()
        currentFragment?.applyWindowInsets(statusBarHeight)
    }

    override fun setTranslucentStatusBar(enable: Boolean) {
        isStatusBarTranslucent = enable
        updateStatusBar()
    }


    private fun updateStatusBar() {
        if (statusBarHeight != 0) {
            updateTranslucentFlag()
            updateNoLimitsFlag()
        }
    }

    private fun updateTranslucentFlag() {
        window.decorView.systemUiVisibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isInMultiWindowMode || !isStatusBarTranslucent) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            0
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun updateNoLimitsFlag() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isInMultiWindowMode) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isInMultiWindowMode) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
    }
}