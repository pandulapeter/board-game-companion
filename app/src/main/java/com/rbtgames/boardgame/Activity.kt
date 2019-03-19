package com.rbtgames.boardgame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.rbtgames.boardgame.databinding.ActivityBinding
import com.rbtgames.boardgame.feature.Fragment
import com.rbtgames.boardgame.feature.home.HomeFlowFragment
import com.rbtgames.boardgame.utils.handleReplace

class Activity : AppCompatActivity() {

    private val currentFragment get() = supportFragmentManager.findFragmentById(R.id.fragment_container) as? Fragment<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityBinding>(this, R.layout.activity)
        if (savedInstanceState == null) {
            supportFragmentManager.handleReplace { HomeFlowFragment.newInstance() }
        }
    }

    override fun onBackPressed() {
        if (currentFragment?.onBackPressed() != true) {
            super.onBackPressed()
        }
    }
}