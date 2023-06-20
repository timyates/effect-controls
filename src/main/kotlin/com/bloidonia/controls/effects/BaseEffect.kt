package com.bloidonia.controls.effects

import com.bloidonia.controls.EffectContainer
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.canvas.Canvas
import javafx.scene.control.Control
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color

abstract class BaseEffect<T : Control> : StackPane() {

    protected val canvas = Canvas()
    protected var inside = false
    protected var x: Double = 0.0
    protected var y: Double = 0.0

    init {
        canvas.isMouseTransparent = true
        background = Background(BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY))
    }

    abstract fun handle(container: EffectContainer<T>, now: Long)

    open fun layout(container: EffectContainer<T>) {
        canvas.heightProperty().bind(container.delegate.heightProperty())
        canvas.widthProperty().bind(container.delegate.widthProperty())
        children.addAll(container.delegate, canvas)
        container.delegate.onMouseMovedProperty()
        container.delegate.onMouseEntered = EventHandler<MouseEvent> { inside = true }
        container.delegate.onMouseExited = EventHandler<MouseEvent> { inside = false }
        container.delegate.onMouseMoved = EventHandler<MouseEvent> {
            x = it.x
            y = it.y
        }
    }

}