package com.almasb.fxglgames.cannon

import com.almasb.fxgl.dsl.components.ExpireCleanComponent
import com.almasb.fxgl.dsl.components.LiftComponent
import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.dsl.getInput
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.entity.components.CollidableComponent
import com.almasb.fxgl.physics.PhysicsComponent
import com.almasb.fxgl.physics.box2d.dynamics.BodyType
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.util.Duration.seconds

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CannonFactory : EntityFactory {
    var liftComponent = LiftComponent()
    //liftComponent. .enable(150.0, seconds(1.0), 100.0)  //distance: Double, duration: Duration, speed: Double

    @Spawns("cannon")
    fun newCannon(data: SpawnData): Entity? {  //remove ? from SpawnData
        return entityBuilder()
                .type(CannonType.CANNON)
                .from(data)
                .view(Rectangle(70.0, 30.0, Color.BROWN))
                //COMMENTED OUT IN ORIG WRKNG JAVA
                // .with( LiftComponent().xAxisSpeedDuration(50.0, seconds(10.0)).yAxisSpeedDuration(250.0, seconds(10.0)) )   //errors! gmknote: orig
                // .with( liftComponent.yAxisDistanceDuration(150.0, seconds(1.0)) )
                .build()
    }

    @Spawns("bullet")
    fun newBullet(data: SpawnData): Entity? {
        val physics = PhysicsComponent()
        physics.setFixtureDef(FixtureDef().density(0.05f))
        physics.setBodyType(BodyType.DYNAMIC)

        physics.setOnPhysicsInitialized {
              val mousePosition: Point2D = getInput().mousePositionWorld
              val pNorm  = Point2D(data.x, data.y).normalize()

              println("pNorm = $pNorm")

              physics.setLinearVelocity(mousePosition.subtract(data.x.times(-1.05), pNorm.y.times(700.0)))
        }

        return entityBuilder()
                .type(CannonType.BULLET)
                .from(data)
                .viewWithBBox(Rectangle(25.0, 25.0, Color.BLUE))
                .with(physics, CollidableComponent(true))
                .with(ExpireCleanComponent(seconds(4.0)))
                .build()
    }

    @Spawns("basketBarrier")   //GMK MODE - REMOVED ? from SpawnData
    fun newBasketBarrier(data: SpawnData): Entity? {
        return entityBuilder()
                .type(CannonType.BASKET)
                .from(data)
                .viewWithBBox(Rectangle(100.0, 300.0, Color.RED))
                .with(PhysicsComponent())
                .build()
    }

    @Spawns("basketGround")
    fun newBasketGround(data: SpawnData): Entity? {  // //GMK MODE - REMOVED ? from SpawnData
        return entityBuilder()
                .type(CannonType.BASKET)
                .from(data)
                .viewWithBBox(Rectangle(300.0, 5.0, Color.TRANSPARENT))
                .with(PhysicsComponent(), CollidableComponent(true))
                .build()
    }
}