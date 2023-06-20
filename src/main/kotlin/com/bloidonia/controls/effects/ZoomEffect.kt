package com.bloidonia.controls.effects

import com.bloidonia.controls.EffectContainer
import javafx.scene.SnapshotParameters
import javafx.scene.control.Control
import javafx.scene.image.WritableImage

class ZoomEffect<T : Control> : BaseEffect<T>() {

    private val snapshotParameters: SnapshotParameters = SnapshotParameters()

    private fun clamp(x: Double, w: Double) = if (x < 0) 0.0 else if (x > w) w else x

    override fun handle(container: EffectContainer<T>, now: Long) {
        if (inside) {
            val snapshot: WritableImage = container.delegate.snapshot(snapshotParameters, null)

            canvas.graphicsContext2D.apply {
                drawImage(
                    snapshot,
                    clamp(x - 50.0, canvas.width - 100),
                    clamp(y - 50.0, canvas.height - 100),
                    100.0,
                    100.0,
                    0.0, 0.0, canvas.width, canvas.height
                )
            }
        } else {
            canvas.graphicsContext2D.clearRect(0.0, 0.0, canvas.width, canvas.height)
        }
    }

}