package com.rbtgames.boardgame

import androidx.room.Room
import com.rbtgames.boardgame.data.persistence.Database
import com.rbtgames.boardgame.data.repository.GameRepository
import com.rbtgames.boardgame.feature.home.games.gameList.GameListViewModel
import com.rbtgames.boardgame.feature.home.games.newGame.NewGameViewModel
import com.rbtgames.boardgame.feature.home.games.playerDetail.PlayerDetailViewModel
import com.rbtgames.boardgame.feature.home.glossary.GlossaryViewModel
import com.rbtgames.boardgame.feature.home.ruleBook.RuleBookViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.experimental.builder.viewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.experimental.builder.single

val persistenceModule = module {
    single { Room.databaseBuilder(androidContext(), Database::class.java, "gameDatabase.db").build() }
}

val dataModule = module {
    single<GameRepository>()
}

val featureModule = module {
    viewModel<GameListViewModel>()
    viewModel<NewGameViewModel>()
    viewModel { (playerId: String, gameId: String) -> PlayerDetailViewModel(get(), playerId, gameId) }
    viewModel<GlossaryViewModel>()
    viewModel<RuleBookViewModel>()
}