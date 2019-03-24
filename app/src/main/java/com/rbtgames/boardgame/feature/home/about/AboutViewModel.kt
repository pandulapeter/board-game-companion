package com.rbtgames.boardgame.feature.home.about

import com.rbtgames.boardgame.BuildConfig
import com.rbtgames.boardgame.feature.ScreenViewModel

class AboutViewModel : ScreenViewModel() {

    val text = "v${BuildConfig.VERSION_NAME}\nBuilt on ${BuildConfig.BUILD_TIME}"
}