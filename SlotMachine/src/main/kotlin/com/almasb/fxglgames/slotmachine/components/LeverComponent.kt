package com.almasb.fxglgames.slotmachine.components

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.components.ViewComponent
import com.almasb.fxglgames.slotmachine.SlotMachineApp
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class LeverComponent : Component() {
    private val view: ViewComponent? = null
    private var currentTextureName = "lever0.png"
    override fun onAdded() {
        view!!.addEventHandler(MouseEvent.MOUSE_CLICKED, EventHandler { e: MouseEvent -> trigger() })
    }

    fun trigger() {
        if (FXGL.getAppCast<SlotMachineApp>().isMachineSpinning()) return
        currentTextureName = if (currentTextureName == "lever0.png") "lever1.png" else "lever0.png"
        view!!.clearChildren()
        view.addChild(FXGL.texture(currentTextureName))
        FXGL.getAppCast<SlotMachineApp>().spin()
    }
}