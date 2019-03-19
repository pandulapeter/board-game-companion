package com.rbtgames.boardgame

import com.rbtgames.boardgame.feature.home.games.gameList.GameListViewModel
import com.rbtgames.boardgame.feature.home.games.newGame.NewGameViewModel
import com.rbtgames.boardgame.feature.home.glossary.GlossaryViewModel
import com.rbtgames.boardgame.feature.home.ruleBook.RuleBookViewModel
import org.koin.androidx.viewmodel.experimental.builder.viewModel
import org.koin.dsl.module.module

val featureModule = module {
    viewModel<GameListViewModel>()
    viewModel<NewGameViewModel>()
    viewModel<GlossaryViewModel>()
    viewModel<RuleBookViewModel>()
}