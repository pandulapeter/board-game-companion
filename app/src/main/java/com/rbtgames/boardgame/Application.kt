package com.rbtgames.boardgame

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.squareup.leakcanary.LeakCanary
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.startKoin

@Suppress("unused")
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
        @Suppress("ConstantConditionIf")
        if (BuildConfig.BUILD_TYPE != "debug") {
            Fabric.with(this, Crashlytics())
        }
        startKoin(
            this, listOf(
                persistenceModule,
                dataModule,
                featureModule
            )
        )
    }
}