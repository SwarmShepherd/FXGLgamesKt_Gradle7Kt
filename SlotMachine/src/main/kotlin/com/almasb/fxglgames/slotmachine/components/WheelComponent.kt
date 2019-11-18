package com.almasb.fxglgames.slotmachine.components

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.components.TransformComponent
import com.almasb.fxglgames.slotmachine.SlotMachineApp
import java.util.*

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class WheelComponent : Component() {
    // [0..9]
    private var value = 0
    private val speed = 10

    var isSpinning = false
        private set

    private val random = Random()

    private val position: TransformComponent? = null

    override fun onUpdate(tpf: Double) {
        if (!isSpinning) return
        val newPosition = value * 260.toDouble()
        val diff = -newPosition - (position!!.y - 70)
        if (diff > 0) {
            position.translateY(speed.toDouble())
        } else if (diff < 0) {
            position.translateY(-speed.toDouble())
        } else {
            isSpinning = false
            FXGL.getAppCast<SlotMachineApp>().onSpinFinished(value)
        }
    }

    fun spin() {
        isSpinning = true
        value = random.nextInt(10)
        // slightly better chances for player
        // since we have two same sprites within the wheel
        if (value == 6) value = 4
    }
}