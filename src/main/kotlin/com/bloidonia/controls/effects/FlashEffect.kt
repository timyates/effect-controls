package com.bloidonia.controls.effects

import com.bloidonia.controls.EffectContainer
import javafx.scene.control.Control
import javafx.scene.effect.BlendMode
import javafx.scene.image.Image
import javafx.scene.paint.Color

class FlashEffect<T : Control> : BaseEffect<T>() {

    private val lightWidth = 100.0
    private val halfWidth = 50.0
    private val blur: Image by lazy { Image(FlashEffect::class.java.getResourceAsStream("/com/bloidonia/blur-circle.png")) }

    override fun handle(container: EffectContainer<T>, now: Long) {
        canvas.blendMode = BlendMode.OVERLAY
        canvas.graphicsContext2D.run {
            fill = Color.BLACK
            fillRect(0.0, 0.0, canvas.width, canvas.height)
            if (inside) {
                fill = Color.WHITE
                fillOval(x - halfWidth, y - halfWidth, lightWidth, lightWidth)
            }
        }
    }

}
