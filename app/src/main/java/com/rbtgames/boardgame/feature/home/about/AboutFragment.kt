package com.rbtgames.boardgame.feature.home.about

import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentAboutBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutFragment : ScreenFragment<FragmentAboutBinding, AboutViewModel>(R.layout.fragment_about) {

    override val viewModel by viewModel<AboutViewModel>()

    companion object {
        fun newInstance() = AboutFragment()
    }
}