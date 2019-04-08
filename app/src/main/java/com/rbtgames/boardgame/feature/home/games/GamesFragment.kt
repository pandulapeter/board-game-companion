package com.rbtgames.boardgame.feature.home.games

import android.os.Bundle
import android.view.View
import com.google.android.material.appbar.AppBarLayout
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.FragmentGamesBinding
import com.rbtgames.boardgame.feature.FlowFragment
import com.rbtgames.boardgame.feature.newGame.NewGameFragment
import com.rbtgames.boardgame.utils.handleReplace

class GamesFragment : FlowFragment<FragmentGamesBinding>(R.layout.fragment_games) {

    private val onOffsetChangedListener = AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
        if (verticalOffset > -appBarLayout.totalScrollRange / 2) {
            binding.floatingActionButton.extend()
        } else {
            binding.floatingActionButton.shrink()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener { navigateToNewGame() }
        binding.appBarLayout.addOnOffsetChangedListener(onOffsetChangedListener)
        binding.viewPager.adapter = GamesPagerAdapter(requireContext(), childFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun onDestroyView() {
        binding.appBarLayout.removeOnOffsetChangedListener(onOffsetChangedListener)
        super.onDestroyView()
    }

    private fun navigateToNewGame() = activityFragmentManager?.handleReplace(addToBackStack = true) { NewGameFragment.newInstance() }

    companion object {
        fun newInstance() = GamesFragment()
    }
}