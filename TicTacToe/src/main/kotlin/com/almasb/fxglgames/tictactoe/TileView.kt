package com.almasb.fxglgames.tictactoe

import com.almasb.fxgl.dsl.FXGL
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Arc
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.util.Duration


/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class TileView(tile: TileEntity) : StackPane() {

    private val app: TicTacToeApp = FXGL.getAppCast() //GKMOD  to clean up after other issues taken care of.

    private val arc   = Arc(34.0, 37.0, 34.0, 37.0, 0.0, 0.0)
    private val line1 = Line(0.0, 0.0, 0.0, 0.0)
    private val line2 = Line(75.0, 0.0, 75.0, 0.0)

    init {
        //app = FXGL.getAppCast()
        val bg  = Rectangle((FXGL.getAppWidth() / 3).toDouble(), (FXGL.getAppHeight() / 3).toDouble(), Color.rgb(13,  222, 236))
        val bg2 = Rectangle((FXGL.getAppWidth() / 4).toDouble(), (FXGL.getAppHeight() / 4).toDouble(), Color.rgb(250, 250, 250, 0.25))
        bg2.arcWidth      = 25.0
        bg2.arcHeight     = 25.0
        arc.fill          = null
        arc.stroke        = Color.BLACK
        arc.strokeWidth   = 3.0
        line1.strokeWidth = 3.0
        line2.strokeWidth = 3.0
        line1.isVisible   = false
        line2.isVisible   = false
        children.addAll(bg, bg2, arc, line1, line2)

        //GKMOD  NOTE: Changes made without understanding why
        tile.getComponent(TileValueComponent::class.java).valueProperty().addListener(ChangeListener<TileValue?>
        {
            _ : ObservableValue<out TileValue>?,
            _ : TileValue?,
            newValue: TileValue? -> animate(newValue!!)
        })

        onMouseClicked = EventHandler { e: MouseEvent -> app.onUserMove(tile) }
    }

    fun animate(value: TileValue) {
        if (value === TileValue.O) {
            val frame = KeyFrame(Duration.seconds(0.5),
                    KeyValue(arc.lengthProperty(), 360))
            val timeline = Timeline(frame)
            timeline.play()
        } else {
            line1.isVisible = true
            line2.isVisible = true
            val frame1 = KeyFrame(Duration.seconds(0.5),
                    KeyValue(line1.endXProperty(), 75),
                    KeyValue(line1.endYProperty(), 75))
            val frame2 = KeyFrame(Duration.seconds(0.5),
                    KeyValue(line2.endXProperty(), 0),
                    KeyValue(line2.endYProperty(), 75))
            val timeline = Timeline(frame1, frame2)
            timeline.play()
        }
    }
}