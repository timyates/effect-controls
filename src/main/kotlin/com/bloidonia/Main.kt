package com.bloidonia

import com.bloidonia.controls.EffectContainer
import com.bloidonia.controls.effects.FlashEffect
import com.bloidonia.controls.effects.WaterEffect
import com.bloidonia.controls.effects.ZoomEffect
import eu.hansolo.tilesfx.Tile
import eu.hansolo.tilesfx.Tile.SkinType
import eu.hansolo.tilesfx.TileBuilder
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.stage.Stage
import kotlin.random.Random
import kotlin.random.asJavaRandom


class Main : Application() {

    override fun start(primaryStage: Stage?) {
        val tile1 = makeTile()
        val tile2 = makeTile()
        val tile3 = makeTile()

        var lastTimerCall = 0L
        val random = Random.asJavaRandom()

        val effectContainer1 = EffectContainer(tile1, WaterEffect(), true)
        val effectContainer2 = EffectContainer(tile2, FlashEffect(), true)
        val effectContainer3 = EffectContainer(tile3, ZoomEffect(), true)
        val box = HBox(effectContainer1, effectContainer2, effectContainer3)

        val timer: AnimationTimer = object : AnimationTimer() {
            override fun handle(now: Long) {
                effectContainer1.effect.handle(effectContainer1, now)
                effectContainer2.effect.handle(effectContainer2, now)
                effectContainer3.effect.handle(effectContainer3, now)
                if (now > lastTimerCall + 1_000_000_000L) {
                    effectContainer1.delegate.value = random.nextDouble() * 25 + 5
                    effectContainer2.delegate.value = random.nextDouble() * 25 + 5
                    effectContainer3.delegate.value = random.nextDouble() * 25 + 5
                    lastTimerCall = now
                }
            }
        }
        timer.start()

        val scene = Scene(box, 450.0, 150.0)
        primaryStage?.title = "Watery TilesFX"
        primaryStage?.scene = scene
        primaryStage?.show()
    }

    private fun makeTile(): Tile {
        val tile = TileBuilder.create()
            .skinType(SkinType.GAUGE)
            .prefSize(150.0, 150.0)
            .backgroundImage(Image(Main::class.java.getResourceAsStream("JavaChampion.png")))
            .backgroundImageKeepAspect(true)
            .infoRegionEventHandler { e ->
                val type = e.eventType
                if (type == MouseEvent.MOUSE_PRESSED) {
                    println("Info Region pressed")
                }
            }
            .infoRegionTooltipText("Info Region")
            .build()
        tile.showNotifyRegion(true)
        tile.showInfoRegion(true)
        return tile
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }

    }

}

