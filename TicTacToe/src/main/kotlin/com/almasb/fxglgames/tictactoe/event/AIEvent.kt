package com.almasb.fxglgames.tictactoe.event

import javafx.event.Event
import javafx.event.EventType

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class AIEvent(eventType: EventType<out Event?>?) : Event(eventType) {
    companion object {
        val ANY = EventType<AIEvent>(Event.ANY, "AI_EVENT")
        val WAITING = EventType(ANY, "AI_EVENT_WAITING")
        val MOVED   = EventType(ANY, "AI_EVENT_MOVED")

        //GKMOD - TESTING!
        //val FINISHED = EventType(ANY, "AI_EVENT_FINISHED")
    }
}