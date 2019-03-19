package com.rbtgames.boardgame

import com.rbtgames.boardgame.feature.home.games.GamesViewModel
import com.rbtgames.boardgame.feature.home.glossary.GlossaryViewModel
import com.rbtgames.boardgame.feature.home.ruleBook.RuleBookViewModel
import org.koin.androidx.viewmodel.experimental.builder.viewModel
import org.koin.dsl.module.module

val featureModule = module {
    viewModel<GamesViewModel>()
    viewModel<GlossaryViewModel>()
    viewModel<RuleBookViewModel>()
}