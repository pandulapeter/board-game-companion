package com.rbtgames.boardgame.feature.home.glossary

import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentGlossaryBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class GlossaryFragment : ScreenFragment<FragmentGlossaryBinding, GlossaryViewModel>(R.layout.fragment_glossary) {

    override val viewModel by viewModel<GlossaryViewModel>()

    companion object {
        fun newInstance() = GlossaryFragment()
    }
}