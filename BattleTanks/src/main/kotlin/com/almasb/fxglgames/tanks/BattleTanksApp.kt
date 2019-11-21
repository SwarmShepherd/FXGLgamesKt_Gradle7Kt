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

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.level.text.TextLevelLoader
import com.almasb.fxgl.input.UserAction
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.CollisionHandler
import com.almasb.fxgl.physics.HitBox
import javafx.geometry.Point2D
import javafx.scene.input.KeyCode

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
class BattleTanksApp : GameApplication() {
    private var playerComponent: PlayerComponent? = null

    override fun initSettings(settings: GameSettings) {
        settings.title   = "BattleTanks"
        settings.version = "0.2"
        settings.width   = 840
        settings.height  = 840
    }

    override fun initInput() {
        val input = FXGL.getInput()

        input.addAction(object : UserAction("Move Left") {
            override fun onAction() {playerComponent!!.left()}
        }, KeyCode.A)

        input.addAction(object : UserAction("Move Right") {
            override fun onAction() {playerComponent!!.right()  }
        }, KeyCode.D)

        input.addAction(object : UserAction("Move Up") {
            override fun onAction() { playerComponent!!.up()}
        }, KeyCode.W)

        input.addAction(object : UserAction("Move Down") {
            override fun onAction() {playerComponent!!.down()}
        }, KeyCode.S)

        input.addAction(object : UserAction("Shoot") {
            override fun onActionBegin() {playerComponent!!.shoot()}
        }, KeyCode.F)

    }

    override fun initGame() {

        FXGL.getGameWorld().addEntityFactory(BattleTanksFactory())


        val level = FXGL.getAssetLoader().loadLevel("level0.txt", TextLevelLoader(84, 84, '0'))
        FXGL.getGameWorld().setLevel(level)

        //1 Instantiate playerComponent
        playerComponent = PlayerComponent()

        //2 Instantiate player (of type Entity)
        val player = Entity()

        player.boundingBoxComponent.addHitBox(HitBox("BODY", Point2D(10.0, 10.0), BoundingShape.box(54.0, 54.0)))

        player.addComponent(playerComponent)

        FXGL.getGameWorld().addEntity(player)
    }

    override fun initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(object : CollisionHandler(BattleTanksType.BULLET, BattleTanksType.WALL) {
            override fun onCollisionBegin(bullet: Entity, wall: Entity) {
                bullet.removeFromWorld()
                wall.removeFromWorld()
            }
        })
    }

} //End_of_Class( BattleTanksApp )


fun main(args: Array<String>) {
    GameApplication.launch(BattleTanksApp::class.java, args)
}
