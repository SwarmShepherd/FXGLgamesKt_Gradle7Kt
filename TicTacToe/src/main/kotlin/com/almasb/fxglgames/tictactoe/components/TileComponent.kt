package com.almasb.fxglgames.tictactoe.components

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import com.almasb.fxglgames.tictactoe.TileValue
import com.almasb.fxglgames.tictactoe.TileValueComponent

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
@Required(TileValueComponent::class)
class TileComponent : Component() {
    /**
     * @param value tile value
     * @return true if marking succeeded
     */
    fun mark(value: TileValue?): Boolean {
        val valueComponent: TileValueComponent = getEntity().getComponent(TileValueComponent::class.java)
        if (valueComponent.value !== TileValue.NONE) return false
        valueComponent.value = value
        return true
    }
}