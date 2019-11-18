package com.almasb.fxglgames.drop

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.core.util.BiConsumer
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.particle.ParticleSystem
import com.almasb.fxgl.time.LocalTimer
import javafx.util.Duration
import java.util.*

/**
 * This is an FXGL version of the libGDX simple game tutorial, which can be found
 * here - https://github.com/libgdx/libgdx/wiki/A-simple-game
 *
 * The player can move the bucket left and right to catch water droplets.
 * There are no win/lose conditions.
 *
 * Note: for simplicity's sake all of the code is kept in this file.
 * In addition, most of typical FXGL API is not used to avoid overwhelming
 * FXGL beginners with a lot of new concepts to learn.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */

class DropApp : GameApplication() {   
    /**
     * Types of entities in this game.
     */
    private lateinit var bucket: Entity

    enum class DropType { DROPLET, BUCKET }


    private val random: Random = Random()  //GKNOTE: 


    //== DEFINE PRIVATE METHODS ======================
    private fun spawnBucket(): Entity? {
        return entityBuilder()
                .type(DropType.BUCKET)
                .at(getAppWidth() / 2.0, getAppHeight() - 200.0)
                .viewWithBBox("bucket.png")
                .collidable()
                .buildAndAttach()
    }

    private fun spawnDroplet(): Entity? {
        return entityBuilder()
                .type(DropType.DROPLET)
                .at(random.nextInt(getAppWidth() - 64).toDouble(), 0.0)  //GMKMOD
                .viewWithBBox("droplet.png")
                .collidable()
                .buildAndAttach()
    }

    //== GameApplication Method Overrides ==
    override fun initSettings(settings: GameSettings) {
        with(settings) {
            title   = "Trophen_Kt"
            version = "1.0"
            width   = 480
            height  = 800
        }
    }

    override fun initGame() {
        bucket = spawnBucket()!!          //GKNOTE; !! makes compiler happy

        //GKNOTE:  Java original was:
        //run(() -> spawnDroplet(), Duration.seconds(1));

        //GKNOTE to translation:  IntelliJ converter could not help until environment fully tweaked...
        //Note also: The Runnable interface should be implemented by any class whose instances are intended to be executed by a thread.
        FXGL.run(Runnable { spawnDroplet() }, Duration.seconds(1.0))

        //Failed to load music bgm.mp3 Error: MediaException: UNKNOWN : com.sun.media.jfxmedia.MediaException:
        // Could not create player! : com.sun.media.jfxmedia.MediaException: Could not create player!
        //loopBGM("bgm.mp3")     //Back Ground Music, fairly long,
    }

    //Problematic Java method
    override fun initPhysics() {
        // GKNOTE: Original Java
        //        onCollisionBegin(DropType.BUCKET, DropType.DROPLET, (bucket, droplet) -> {
        //            droplet.removeFromWorld();       
        //            play("drop.wav");
        //        });

        with(FXGL) {
            onCollisionBegin(DropType.BUCKET, DropType.DROPLET, BiConsumer { _: Entity, droplet: Entity ->
                droplet.removeFromWorld()
                play("drop.wav")   //GKNOTE: Fast single drip sound
            })
        }
    }

    override fun onUpdate(tpf: Double) {
        // GKNOTE: Original Java
        //    @Override
        //    protected void onUpdate(double tpf) {
        //        getGameWorld().getEntitiesByType(DropType.DROPLET).forEach(droplet -> droplet.translateY(150 * tpf));
        //
        //        bucket.setX(getInput().getMouseXWorld());
        //    }

        val droplets = getGameWorld().getEntitiesByType(DropType.DROPLET)
        droplets.forEach {
            it.translateY(150 * tpf)
        }

        //GKNOTE: This took some MANUAL research
        //bucket.x =  getInput().getMouseXWorld()
        bucket.x = FXGL.getInput().mousePositionWorld.x
    }
}

fun main(args: Array<String>) {
    GameApplication.launch(DropApp::class.java, args)
}

