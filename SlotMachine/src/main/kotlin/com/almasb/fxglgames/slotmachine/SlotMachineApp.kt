package com.almasb.fxglgames.slotmachine

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getAppWidth
import com.almasb.fxglgames.slotmachine.components.WheelComponent
import javafx.beans.value.ObservableValue
import javafx.geometry.Bounds
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import java.util.stream.Collectors


/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class SlotMachineApp : GameApplication() {

    private var entityFactory: SlotMachineFactory? = null

    override fun initSettings(settings: GameSettings) {
        settings.title   = "Slot Machine (Kotlin)"
        settings.version = "0.1"
        settings.width   = 1280
        settings.height  = 720
    }


    //protected void initGameVars(Map<String, Object> vars) {}
    override fun initGameVars(vars: MutableMap<String?, Any?>) {
        vars["money"] = START_MONEY
    }

    override fun initGame() {
        entityFactory = SlotMachineFactory()

        //GKNOTE
        for (i in 0..4 ) {
            FXGL.getGameWorld().addEntities(entityFactory!!.run { buildWheels()[i] })
        }

        //FXGL.getGameWorld().addEntities(*entityFactory!!.buildWheels().toTypedArray())

        FXGL.getGameWorld().addEntities(*entityFactory!!.buildBackground())

        FXGL.getGameWorld().addEntity(entityFactory!!.buildLever())

    }

    override fun initUI() {
        val textMoney = Text()

        //Orig Java..
        //textMoney.layoutBoundsProperty().addListener((observable, oldValue, newBounds) -> {
        //    textMoney.setTranslateX(getAppWidth() / 2 - newBounds.getWidth() / 2);
        //});
        textMoney.layoutBoundsProperty().addListener { _ : ObservableValue<out Bounds>?, _: Bounds, newBounds: Bounds -> textMoney.translateX = getAppWidth() / 2 - newBounds.width / 2 }

        textMoney.translateY = 50.0
        textMoney.font = Font.font(36.0)
        textMoney.fill = Color.WHITE
        textMoney.textProperty().bind(FXGL.getGameState().intProperty("money").asString("$%d"))
        FXGL.getGameScene().addUINode(textMoney)
    }


    //private val spinValues: List<Int> = ArrayList()
    //GKMOD to mutableList
    private val spinValues = mutableListOf<Int>()


    fun isMachineSpinning() : Boolean {
        return getWheels()?.stream()!!.anyMatch(WheelComponent::isSpinning)  //?!!
    }

    fun spin() {
        getWheels()?.forEach { obj: WheelComponent -> obj.spin() }
    }

    //Java Code
    //    open fun getWheels(): List<WheelComponent?>? {
    //        return getGameWorld()
    //                .getEntitiesByType(SlotMachineType.WHEEL)
    //                .stream()
    //                .map(e -> e.getComponent(WheelComponent.class))
    //                .collect(Collectors.toList())
    //    }
    private fun getWheels() : List<WheelComponent>? {
        return with(FXGL) { getGameWorld().getEntitiesByType(SlotMachineType.WHEEL) }
                .map {e -> e.getComponent(WheelComponent::class.java ) }
    }

    //=============================================================

    fun onSpinFinished(value: Int) {
        spinValues.add(value)
        if (spinValues.size == 5) {
            spinValues.stream()
                    .collect(Collectors.groupingBy { i: Int -> i })
                    .values
                    .stream()
                    .mapToInt { obj: List<Int> -> obj.size }
                    .max()
                    .ifPresent(::giveMoney)
            spinValues.clear()
        }
    }

    private fun giveMoney(highestMatch: Int) {
        val reward = if (highestMatch > 1) {
            highestMatch * highestMatch * 50
        } else {
            -100
        }
        FXGL.getGameState().increment("money", reward)
    }

    companion object {
        private const val START_MONEY = 500
    }

}

fun main(args: Array<String>) {
    GameApplication.launch(SlotMachineApp::class.java, args)
}