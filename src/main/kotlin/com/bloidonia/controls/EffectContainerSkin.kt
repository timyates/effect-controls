package com.bloidonia.controls

import javafx.scene.control.Control
import javafx.scene.control.Skin
import javafx.scene.control.SkinBase

class EffectContainerSkin<T : Control>(container: EffectContainer<T>) : SkinBase<EffectContainer<T>>(container), Skin<EffectContainer<T>> {

    init {
        container.effect.layout(container)
        children.add(container.effect)
        container.effect.prefWidth = container.delegate.prefWidth
        container.effect.minWidth = container.delegate.minWidth
        container.effect.maxWidth = container.delegate.maxWidth
        container.effect.prefHeight = container.delegate.prefHeight
        container.effect.minHeight = container.delegate.minHeight
        container.effect.maxHeight = container.delegate.maxHeight
    }

}