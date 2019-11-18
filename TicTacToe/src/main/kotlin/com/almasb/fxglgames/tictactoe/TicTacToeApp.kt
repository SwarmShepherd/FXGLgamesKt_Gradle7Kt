package com.almasb.fxglgames.tictactoe


import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.entity.Entity
import com.almasb.fxglgames.tictactoe.components.enemy.MinimaxComponent
import com.almasb.fxglgames.tictactoe.event.AIEvent
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.util.Duration
import java.util.*


/**
 * An example of a UI based game.
 * A classic game of Tic-tac-toe.
 * Comes with 2 types of AI: rule based and minimax based.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class TicTacToeApp : GameApplication() {

    private var playerStarts = true
    private var firstTime    = true

    companion object {  //GKMOD TO PUT INTO COMPANION OBJECT AS KOTLIN ROUGH EQUIV OF STATICS (accessed on the Class)
        var board = Array(3) { arrayOfNulls<TileEntity?>(3) }

        //val combos: List<TileCombo> = ArrayList()
        var combos: ArrayList<TileCombo> = ArrayList() //GKMOD from above
    }

    override fun initSettings(settings: GameSettings) {
        with(settings) {
            title   = "My ToeTicTac"
            version = "0.3"
            width   =  600
            height  =  600
        }
    }

    fun checkGameFinished(): Boolean {
        for (combo in combos) {
            if (combo.isComplete) {
                playWinAnimation(combo)
                return true
            }
        }
        for (y in 0..2) {
            for (x in 0..2) {
                val tile = board[x][y]!!
                if (tile.value === TileValue.NONE) {
                    return false  // at least 1 tile is empty
                }
            }
        }
        gameOver("DRAW")
        return true
    }

    //Rules for a Lambda...
    //  1. no fun modifier like private, void and so
    //  2. no return type
    //  3. no method name

    override fun initGame() {

        //ORIGINAL JAVA
        //        if (firstTime) {
        //            getEventBus().addEventHandler(AIEvent.MOVED, event -> checkGameFinished());
        //            firstTime = false;
        //        }

        //GNNODE:  FXGL specific info on events here:  https://github.com/AlmasB/FXGL/wiki/Events-and-Communication

        //NEW IntelliJ translation, from original java - FIXES THE GAME END RECOGNITION BUT INTRODUCES A BUG
        if (firstTime) {
            //getEventBus().addEventHandler(AIEvent.MOVED, EventHandler { event: AIEvent -> checkGameFinished() })
            getEventBus().addEventHandler(AIEvent.MOVED, EventHandler { checkGameFinished() })
            firstTime = false
        }

        for (y in 0..2) {
            for (x in 0..2) {
                val tile = TileEntity(x * getAppWidth() / 3.0, y * getAppHeight() / 3.0)
                board[x][y] = tile
                getGameWorld().addEntity(tile)
            }
        }

        val enemy = Entity()

        // this controls the AI behavior
        enemy.addComponent( MinimaxComponent() )
        getGameWorld().addEntity(enemy)

        combos.clear()

        // horizontal
        for (y in 0..2) {
            combos.add(TileCombo(board[0][y], board[1][y], board[2][y]))
        }

        // vertical
        for (x in 0..2) {
            combos.add(TileCombo(board[x][0], board[x][1], board[x][2]))
        }

        // diagonals
        combos.add(TileCombo(board[0][0], board[1][1], board[2][2]))
        combos.add(TileCombo(board[2][0], board[1][1], board[0][2]))

//        playerStarts = !playerStarts
//        if (playerStarts) {aiMove)

        playerStarts = if (playerStarts) {
            false
        } else {
            aiMove()
            true
        }
    }

    override fun initUI() {
        val line1 = Line(getAppWidth() / 3.0, 0.0, getAppWidth() / 3.0, 0.0)
        val line2 = Line(getAppWidth() / 3.0 * 2, 0.0, getAppWidth() / 3.0 * 2, 0.0)
        val line3 = Line(0.0, getAppHeight() / 3.0, 0.0, getAppHeight() / 3.0)
        val line4 = Line(0.0, getAppHeight() / 3.0 * 2, 0.0, getAppHeight() / 3.0 * 2)
        getGameScene().addUINodes(line1, line2, line3, line4)

        // animation
        val frame1 = KeyFrame(Duration.seconds(0.5), KeyValue(line1.endYProperty(), getAppHeight()))
        val frame2 = KeyFrame(Duration.seconds(1.0), KeyValue(line2.endYProperty(), getAppHeight()))
        val frame3 = KeyFrame(Duration.seconds(0.5), KeyValue(line3.endXProperty(), getAppWidth()))
        val frame4 = KeyFrame(Duration.seconds(1.0), KeyValue(line4.endXProperty(), getAppWidth()))
        val timeline = Timeline(frame1, frame2, frame3, frame4)
        timeline.play()
    }

    fun aiMove() {
        getEventBus().fireEvent(AIEvent(AIEvent.WAITING))
    }

    fun playWinAnimation(combo: TileCombo) {
        println(">playWinAnimation()")
        val line = Line()
        line.startX = combo.tile1!!.center.x
        line.startY = combo.tile1.center.y
        line.endX   = combo.tile1.center.x
        line.endY   = combo.tile1.center.y
        line.stroke = Color.YELLOW
        line.strokeWidth = 3.0
        getGameScene().addUINode(line)
        val timeline = Timeline()
        timeline.keyFrames.add(KeyFrame(Duration.seconds(1.0),
                KeyValue(line.endXProperty(), combo.tile3!!.center.x),
                KeyValue(line.endYProperty(), combo.tile3.center.y)))
        timeline.onFinished = EventHandler { e: ActionEvent -> gameOver(combo.winSymbol!!) } //GKMOD added '!!' to satisfy compiler only, NO idea what I'm doing (much)
        timeline.play()
    }

    fun gameOver(winner : String) {
        println(">gameOver(), winner = $winner")
        //GKMOD - Would not translate
//        getDisplay().showConfirmationBox("Winner: $winner \nContinue?", yes -> {
//            if yes
//                getGameController().startNewGame()
//            else
//                getGameController().exit()
//        }

        getDisplay().showConfirmationBox("Winner: $winner \nContinue?") { yes ->
            if (yes) {
                getGameController().startNewGame()
            } else {
                getGameController().exit()
            }
        }
    }


    fun onUserMove(tile: TileEntity) {
        println(">onUserMove()  tile = $tile")
        val ok = tile.control.mark(TileValue.X)
        if (ok) {
            val over = checkGameFinished()
            if (!over) {
                aiMove()
            }
        }
    }
}

//converted from companion object...
fun main(args: Array<String>) {
    GameApplication.launch(TicTacToeApp::class.java, args)
}