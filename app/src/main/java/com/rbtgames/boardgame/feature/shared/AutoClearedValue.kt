package com.rbtgames.boardgame.feature.shared

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AutoClearedValue<T : Any> : ReadWriteProperty<Fragment, T> {

    private var value: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
        value ?: throw IllegalStateException("Trying to call an auto-cleared value outside of the view lifecycle.")

    @Suppress("unused")
    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        thisRef.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                this@AutoClearedValue.value = null
            }
        })
        this.value = value
    }
}