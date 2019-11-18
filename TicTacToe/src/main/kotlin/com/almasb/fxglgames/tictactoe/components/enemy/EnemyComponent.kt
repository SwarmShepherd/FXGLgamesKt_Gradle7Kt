package com.almasb.fxglgames.tictactoe.components.enemy

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.event.Subscriber
import com.almasb.fxglgames.tictactoe.event.AIEvent
import javafx.event.EventHandler

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
abstract class EnemyComponent : Component() {
    private var eventListener: Subscriber? = null
    override fun onAdded() {
        eventListener = FXGL.getEventBus().addEventHandler(AIEvent.WAITING, EventHandler { event: AIEvent ->
            makeMove()
            FXGL.getEventBus().fireEvent(AIEvent(AIEvent.MOVED))
        })
    }

    override fun onRemoved() {
        eventListener!!.unsubscribe()
    }

    abstract fun makeMove()
}