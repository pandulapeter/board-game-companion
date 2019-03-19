package com.rbtgames.boardgame.feature.home

import android.os.Bundle
import android.view.View
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentHomeFlowBinding
import com.rbtgames.boardgame.feature.FlowFragment
import com.rbtgames.boardgame.feature.home.games.GamesFragment
import com.rbtgames.boardgame.feature.home.glossary.GlossaryFragment
import com.rbtgames.boardgame.feature.home.ruleBook.RuleBookFragment
import com.rbtgames.boardgame.utils.consume
import com.rbtgames.boardgame.utils.handleReplace

class HomeFlowFragment : FlowFragment<FragmentHomeFlowBinding>(R.layout.fragment_home_flow) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.games -> consume { navigateToGames() }
                R.id.glossary -> consume { navigateToGlossary() }
                R.id.rule_book -> consume { navigateToRuleBook() }
                else -> false
            }
        }
        if (savedInstanceState == null && currentFragment == null) {
            navigateToGames()
        }
    }

    private fun navigateToGames() = childFragmentManager.handleReplace { GamesFragment.newInstance() }

    private fun navigateToGlossary() = childFragmentManager.handleReplace { GlossaryFragment.newInstance() }

    private fun navigateToRuleBook() = childFragmentManager.handleReplace { RuleBookFragment.newInstance() }

    companion object {
        fun newInstance() = HomeFlowFragment()
    }
}