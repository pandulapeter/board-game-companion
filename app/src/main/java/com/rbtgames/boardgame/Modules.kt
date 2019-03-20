package com.rbtgames.boardgame

import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.data.repository.GameRepository
import com.rbtgames.boardgame.feature.home.games.gameList.GameListViewModel
import com.rbtgames.boardgame.feature.home.games.newGame.NewGameViewModel
import com.rbtgames.boardgame.feature.home.games.playerDetail.PlayerDetailViewModel
import com.rbtgames.boardgame.feature.home.glossary.GlossaryViewModel
import com.rbtgames.boardgame.feature.home.ruleBook.RuleBookViewModel
import org.koin.androidx.viewmodel.experimental.builder.viewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.experimental.builder.single

val dataModule = module {
    single<GameRepository>()
}

val featureModule = module {
    viewModel<GameListViewModel>()
    viewModel<NewGameViewModel>()
    viewModel { (game: Game, player: Player) -> PlayerDetailViewModel(game, player) }
    viewModel<GlossaryViewModel>()
    viewModel<RuleBookViewModel>()
}