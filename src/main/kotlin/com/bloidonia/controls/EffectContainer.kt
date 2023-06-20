package com.bloidonia.controls

import com.bloidonia.controls.effects.BaseEffect
import com.bloidonia.controls.effects.FlashEffect
import javafx.animation.AnimationTimer
import javafx.scene.control.Control
import javafx.scene.control.Skin

class EffectContainer<T : Control>(
    val delegate: T,
    val effect: BaseEffect<T> = FlashEffect(),
    externalAnimationTimer: Boolean = false
) : Control() {

    private val timer: AnimationTimer? = if (externalAnimationTimer)
        null
    else
        InternalAnimationTimer(this)

    internal class InternalAnimationTimer<T : Control>(private val effectContainer: EffectContainer<T>) : AnimationTimer() {

        override fun handle(now: Long) {
            effectContainer.effect.handle(effectContainer, now)
        }

    }

    init {
        timer?.start()
    }

    override fun createDefaultSkin(): Skin<*> = EffectContainerSkin(this)

}
