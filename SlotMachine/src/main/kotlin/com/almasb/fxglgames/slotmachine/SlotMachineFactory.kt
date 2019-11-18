package com.almasb.fxglgames.slotmachine

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.Entity
import com.almasb.fxglgames.slotmachine.components.LeverComponent
import com.almasb.fxglgames.slotmachine.components.WheelComponent

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class SlotMachineFactory {
    fun buildLever(): Entity {
        return FXGL.entityBuilder()
                .at(1030.0, 340.0)
                .view("lever0.png")
                .with(LeverComponent())
                .build()
    }

    fun buildBackground(): Array<Entity> {
        return arrayOf(
                FXGL.entityBuilder().view("bg.png").build(),
                FXGL.entityBuilder().at(910.0, 410.0).view("coin.gif").build()
        )
    }


    //    public Entity[] buildWheels() {
    //        return IntStream.range(0, 5)
    //                .mapToObj(i -> {
    //                    return FXGL.entityBuilder()
    //                            .type(SlotMachineType.WHEEL)
    //                            .at(50 + 240 * i, 70)
    //                            .view("elements.png")
    //                            .with(new WheelComponent())
    //                            .build();
    //                }).toArray(Entity[]::new);
    //    }
    /**
     * JAVA: IntStream range(int startInclusive, int endExclusive)
     * returns a sequential ordered IntStream from startInclusive (inclusive)
     * to endExclusive (exclusive) by an incremental step of 1.
     */

    //GKNOTE: This returned an Array in Java version...need to check out upstream declarations
    fun  buildWheels() :   MutableList<Entity> {    //No compiler error detected here
        //As translated by IntelliJ BUT range on IntStream is not legal in this Java version
//        return IntStream.range(0, 5)
//                .mapToObj<Any> { i: Int -> }.toArray(IntFunction<Array<Entity?>> { _Dummy_.__Array__() })

        //in Baby Steps
        val list = mutableListOf <Entity >()
        for ( i in 0..5) {
            val entity = FXGL.entityBuilder()
                    .type(SlotMachineType.WHEEL)
                    .at((50 + 240.0 * i), 70.0)
                    .view("elements.png")
                    .with(WheelComponent())
                    .build()
            list.add(entity)
        }

        return list
    }


}
