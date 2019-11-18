package com.almasb.fxglgames.cannon

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.core.util.BiConsumer
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.Entity
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.text.Text
import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.dsl.onBtnDown


/**
 * A basic FXGL game demo.
 *
 * Game:
 * The player shoots from a "cannon" and tries to
 * get the projectile in-between the barriers.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */


class CannonApp : GameApplication() {

    //private lateinit var cannon: Entity   //GMK MOD - SEEMS TO BE SUGGESTED
    private lateinit var cannon : Entity    //GMK MOD

    override fun initSettings(settings: GameSettings) {
        with(settings) {
            width   = 800
            height  = 600
            title   = "Cannon (Kotlin)"
            version = "0.2.1"
        }
    }

    override fun initInput() {
        onBtnDown(MouseButton.PRIMARY, "Shoot") { shoot() }
    }

    //GKTEMP
//    override fun initGameVars(vars: Map<String, Any>) {
//        vars.put("score", 0)
//    }

    override fun initGameVars( vars: MutableMap<String, Any>) {
        //vars.put("score", 0)  gkmod
        vars["score"] = 0
    }

    override fun initGame() {
        getGameWorld().addEntityFactory(CannonFactory())  //GMK MOD HERE
        initScreenBounds()
        initCannon()
        initBasket()
    }

    //=========================================================

    private fun initScreenBounds() {
        //getGameWorld().addEntity(Entities.makeScreenBounds(100));   //ALSO COMMENTED OUT IN WORKING J VERS

    }

    private fun initCannon() {
        cannon = getGameWorld().spawn("cannon", 50.0, FXGL.getAppHeight() - 30.toDouble())
    }

    private fun initBasket() {
        FXGL.spawn("basketBarrier", 400.0, FXGL.getAppHeight() - 300.toDouble())
        FXGL.spawn("basketBarrier", 700.0, FXGL.getAppHeight() - 300.toDouble())
        FXGL.spawn("basketGround",  500.0,  FXGL.getAppHeight().toDouble())
    }


    private fun shoot() {
        FXGL.spawn("bullet", cannon.getPosition().add(70.0, 0.0))
    }

//GMK MOD ORIG
//    fun initPhysics() {
//        com.almasb.fxgl.dsl.FXGL.onCollisionBegin(com.almasb.fxglgames.cannon.CannonType.BULLET, com.almasb.fxglgames.cannon.CannonType.BASKET, com.almasb.fxgl.core.util.BiConsumer({ bullet: com.almasb.fxgl.entity.Entity, basket: com.almasb.fxgl.entity.Entity? ->
//            bullet.removeFromWorld()
//            com.almasb.fxgl.dsl.FXGL.inc("score", +1000)
//        }))
//    }

    override fun initPhysics() {
        FXGL.onCollisionBegin(CannonType.BULLET, CannonType.BASKET, BiConsumer{ bullet: Entity, basket: Entity? ->
            bullet.removeFromWorld()
            FXGL.inc("score", +1000)
        })
    }

    override fun initUI() {
        val scoreText: Text = FXGL.getUIFactory().newText("", Color.BLACK, 24.0)
        //scoreText.setTranslateX(550.0) orig - gmkmod
        //scoreText.translateX = 550.0

        with(scoreText) {
            //scoreText.setTranslateX(550.0) orig - gmkmod
            translateX = 550.0
            translateY = 100.0
            textProperty().bind(FXGL.getGameState().intProperty("score").asString("Score: [%d]"))
        }
        getGameScene().addUINode(scoreText)
    }

    //===========================

    //@kotlin.jvm.JvmStatic    ORIG
    //fun main(args: Array<String>) {
    //    launch(args)
    //}

}


fun main(args: Array<String>) {
    GameApplication.launch(CannonApp::class.java, args)
}
