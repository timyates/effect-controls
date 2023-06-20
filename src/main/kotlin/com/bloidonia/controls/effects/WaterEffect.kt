package com.bloidonia.controls.effects

import com.bloidonia.controls.EffectContainer
import javafx.beans.Observable
import javafx.scene.SnapshotParameters
import javafx.scene.control.Control
import javafx.scene.image.PixelFormat
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import javafx.scene.paint.Paint

class WaterEffect<T : Control>(val background: Paint = Color.grayRgb(0xf4)) : BaseEffect<T>() {

    private lateinit var screenarea: Screen32
    private lateinit var texture: Screen32
    private lateinit var water: Waterpic
    private lateinit var output: WritableImage
    private val snapshotParameters: SnapshotParameters = SnapshotParameters().apply {
        fill = background
    }
    private var w: Int = 0
    private var h: Int = 0
    private val scale = 100
    private lateinit var container: EffectContainer<T>

    override fun layout(container: EffectContainer<T>) {
        super.layout(container)
        this.container = container
        canvas.widthProperty().addListener(this::reshapeListener)
        canvas.heightProperty().addListener(this::reshapeListener)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun reshapeListener(o: Observable) {
        w = canvas.width.toInt()
        h = canvas.height.toInt()
        if (w > 0 && h > 0) {
            screenarea = Screen32(w, h)
            screenarea.clear(0x000000)
            texture = Screen32(w, h)
            water = Waterpic(w * scale / 100, h * scale / 100)
            output = WritableImage(w, h)
            println("W: $w H $h")
        }
    }

    override fun handle(container: EffectContainer<T>, now: Long) {
        if (w != 0 && h != 0) {
            val snapshot: WritableImage = container.delegate.snapshot(snapshotParameters, null)
            texture.load(snapshot)

            water.flip()
            water.render(screenarea, texture)

            output.pixelWriter.setPixels(0,0, w, h, PixelFormat.getIntArgbPreInstance(), screenarea.data, 0, w)
            canvas.graphicsContext2D.drawImage(output, 0.0, 0.0)
            if (inside) {
                water.setDot(x.toInt(), y.toInt(), 450, 10, w, h)
            }
        }
    }

}

/**
 * Water effect taken and adapted from Robert Jeppesen's amazing water applet from back in the day.
 *
 * See: https://github.com/rojepp/java_applets
 *
 * I remember decompiling this before github was a thing to see how it worked.
 */
class Screen32(val width: Int, val height: Int) {
    internal val data: IntArray
    internal val ytab: IntArray
    private var hwidth: Int = 0
    private var hheight: Int = 0
    private val widthheight: Int = width * height
    private var i: Int = 0

    init {
        hheight = height shr 1
        hwidth = width shr 1
        data = IntArray(widthheight)
        ytab = IntArray(height)
        var yval = 0
        i = 0
        while (i < height) {
            ytab[i] = yval
            yval += width
            i++
        }
    }

    fun load(image: WritableImage) {
        image.pixelReader.getPixels(0, 0, width, height, PixelFormat.getIntArgbPreInstance(), data, 0, width)
    }

    fun clear(c: Int) {
        i = this.widthheight
        while (--i >= 0) data[i] = c
    }
}

class Waterpic(val width: Int, val height: Int) {
    val data: IntArray
    private var ytab: IntArray
    private var hwidth: Int = 0
    private var hheight: Int = 0
    private var dim: Int = 0
    private var ptrData: Int = 0
    private var ptrData2: Int = 0
    private var dotsize: Int = 0
    private var dotdepth: Int = 0
    private var ndotsize: Int = 0


    private var ptrIndex: Int = 0
    private var dInd: Int = 0
    private var ptrDestData: Int = 0
    private var v: Int = 0
    private var newx: Int = 0
    private var newy: Int = 0
    private var y: Int = 0
    private var x: Int = 0

    init {
        hwidth = width shr 1
        hheight = height shr 1
        dotsize = 9
        dotdepth = 450
        ndotsize = 6
        dim = 5
        data = IntArray(width * (height + 2) * 4)
        ptrData = width + 1
        ptrData2 = 4 + width * (height + 3)

        ytab = IntArray(height + 2)
        var yval = 0
        for (i in 0 until height + 2) {
            ytab[i] = yval
            yval += width
        }

    }

    fun render(d: Screen32, t: Screen32) {
        ptrIndex = ptrData
        dInd = 0
        ptrDestData = ptrData2

        y = 0
        while (y < height) {
            x = 0
            while (x < width) {
                v = data[ptrIndex - width] +
                        data[ptrIndex + width] +
                        data[ptrIndex - 1] +
                        data[ptrIndex + 1] +
                        data[ptrIndex - width - 1] +
                        data[ptrIndex - width + 1] +
                        data[ptrIndex + width - 1] +
                        data[ptrIndex + width + 1] shr 2

                v -= data[ptrDestData]
                v -= v shr dim
                data[ptrDestData++] = v

                v = 1024 - v

                newx = ((x - hwidth) * v shr 10) + hwidth
                newx = if (newx >= width) width - 1 else if (newx < 0) 0 else newx
                newy = ((y - hheight) * v shr 10) + hheight
                newy = if (newy >= height) height - 1 else if (newy < 0) 0 else newy

                d.data[dInd++] = t.data[newx + t.ytab[newy]]
                ptrIndex++
                x++
            }
            y++
        }
    }

    fun flip() {
        if (ptrData == width + 1) {
            ptrData = ptrData2
            ptrData2 = width + 1
        } else {
            ptrData2 = ptrData
            ptrData = width + 1
        }
    }

    fun setDot(x: Int, y: Int, c: Int, s: Int, appletwidth: Int, appletheight: Int) {
        var x1 = x
        var y1 = y
        y1 = y1 * height / appletheight
        x1 = x1 * width / appletwidth
        val ysize = s * height / appletheight
        val xsize = s * width / appletwidth
        for (i in 0 until ysize)
            for (j in 0 until xsize)
                if (y1 + i + 1 > 0 && y1 + i + 1 < height - ysize)
                    data[ptrData2 + ytab[y1 + i + 1] + x1 + j + 1] += c
    }

}