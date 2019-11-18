package com.almasb.fxglgames.tictactoe

//import com.almasb.fxgl.entity.components.ViewComponent
//GKNOTE:  ViewComponent src here: /home/george/FXGL_Dev/fxgl_lib_src_kotlin/entity/entity/components
//import com.almasb.fxgl.entity.components.*

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.HitBox
import com.almasb.fxglgames.tictactoe.components.TileComponent


/**
 * Instead of using generic GameEntity we add a few convenience methods.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */


//GKNOTE:  Class is instantiated 9x at game initialization
class TileEntity(x: Double, y: Double) : Entity() {
    val value: TileValue?
        get() = getComponent(TileValueComponent::class.java).value

    val control: TileComponent
        get() = getComponent(TileComponent::class.java)

    init {
        setX(x)
        setY(y)
        addComponent(TileValueComponent())

        boundingBoxComponent.addHitBox(HitBox(BoundingShape.box((FXGL.getAppWidth() / 3).toDouble(), (FXGL.getAppWidth() / 3).toDouble())))
        viewComponent.addChild(TileView(this))  //GKNOTE: despite error compiles and runs without issues!?
        addComponent(TileComponent())
    }
}


