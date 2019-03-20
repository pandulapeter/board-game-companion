package com.rbtgames.boardgame

import android.app.Application
import org.koin.android.ext.android.startKoin

@Suppress("unused")
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(
            this, listOf(
                dataModule,
                featureModule
            )
        )
    }
}