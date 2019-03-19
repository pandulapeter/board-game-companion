package com.rbtgames.boardgame.feature.home.ruleBook

import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentRuleBookBinding
import com.rbtgames.boardgame.feature.ScreenFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RuleBookFragment : ScreenFragment<FragmentRuleBookBinding, RuleBookViewModel>(R.layout.fragment_rule_book) {

    override val viewModel by viewModel<RuleBookViewModel>()

    companion object {
        fun newInstance() = RuleBookFragment()
    }
}