package com.rbtgames.boardgame

import androidx.room.Room
import com.rbtgames.boardgame.data.persistence.Database
import com.rbtgames.boardgame.data.repository.GameRepository
import com.rbtgames.boardgame.feature.gameDetail.GameDetailViewModel
import com.rbtgames.boardgame.feature.home.about.AboutViewModel
import com.rbtgames.boardgame.feature.home.games.active.ActiveGamesViewModel
import com.rbtgames.boardgame.feature.home.games.finished.FinishedGamesViewModel
import com.rbtgames.boardgame.feature.home.glossary.GlossaryViewModel
import com.rbtgames.boardgame.feature.home.ruleBook.RuleBookViewModel
import com.rbtgames.boardgame.feature.newGame.NewGameViewModel
import com.rbtgames.boardgame.feature.playerDetail.PlayerDetailViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.experimental.builder.viewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val persistenceModule = module {
    single { Room.databaseBuilder(androidContext(), Database::class.java, "gameDatabase.db").build() }
}

val dataModule = module {
    single { GameRepository(get()) }
}

val featureModule = module {
    viewModel<ActiveGamesViewModel>()
    viewModel<FinishedGamesViewModel>()
    viewModel<NewGameViewModel>()
    viewModel { (playerId: String, gameId: String) -> PlayerDetailViewModel(get(), playerId, gameId) }
    viewModel { (gameId: String) -> GameDetailViewModel(get(), gameId) }
    viewModel<GlossaryViewModel>()
    viewModel<RuleBookViewModel>()
    viewModel<AboutViewModel>()
}