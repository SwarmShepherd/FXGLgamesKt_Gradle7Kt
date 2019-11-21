/*
 * The MIT License (MIT)
 *
 * FXGL - JavaFX Game Library
 *
 * Copyright (c) 2015-2016 AlmasB (almaslvl@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.almasb.fxglgames.tanks

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.components.ViewComponent
import com.almasb.fxgl.texture.Texture
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
class PlayerComponent : Component() {
    private val view: ViewComponent? = null
    private var texture: Texture?   = null

    override fun onAdded() {
        getEntity().transformComponent.rotationOrigin = Point2D(42.0, 42.0)
        texture = FXGL.getAssetLoader().loadTexture("player.png")
        view!!.addChild(texture!!)
    }

    private var speed  = 0.0
    private var frames = 0


    override fun onUpdate(tpf: Double) {
        speed = tpf * 60
        var frame = frames / 10
        if (frame >= 8) {
            frame = 0
            frames = 0
        }
        texture!!.viewport = Rectangle2D((frame * 84).toDouble(), 0.0, 84.0, 84.0)
    }

    fun up() {
        getEntity().rotation = 270.0
        getEntity().translateY(-5 * speed)
        frames++
    }

    fun down() {
        getEntity().rotation = 90.0
        getEntity().translateY(5 * speed)
        frames++
    }

    fun left() {
        getEntity().rotation = 180.0
        getEntity().translateX(-5 * speed)
        frames++
    }

    fun right() {
        getEntity().rotation = 0.0
        getEntity().translateX(5 * speed)
        frames++
    }

    fun shoot() {
        FXGL.spawn("Bullet", SpawnData(getEntity().center).put("direction", angleToVector()))
    }

    private fun angleToVector(): Point2D {
        val angle = getEntity().rotation
        return if (angle == 0.0) {
            Point2D(1.0, 0.0)
        } else if (angle == 90.0) {
            Point2D(0.0, 1.0)
        } else if (angle == 180.0) {
            Point2D(-1.0, 0.0)
        } else { // 270
            Point2D(0.0, -1.0)
        }
    }
}