package com.almasb.fxglgames.tictactoe

import java.util.*

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class TileCombo(val tile1: TileEntity?, val tile2: TileEntity?, val tile3: TileEntity?) {
    private val tiles: List<TileEntity?>?

    val isComplete: Boolean
        get() = tile1!!.value != TileValue.NONE && tile1.value == tile2!!.value && tile1.value == tile3!!.value

    /**
     * @return true if all tiles are empty
     */
    val isOpen: Boolean
        get() = tiles!!.stream()
                .allMatch { t: TileEntity? -> t!!.value == TileValue.NONE }

    /**
     * @param value tile value
     * @return true if this combo has 2 of value and an empty slot
     */
    fun isTwoThirds(value: TileValue?): Boolean {
        val oppositeValue = if (value == TileValue.X) TileValue.O else TileValue.X
        return if (tiles!!.stream().anyMatch { t: TileEntity? -> t!!.value == oppositeValue }) false else tiles.stream()
                .filter { t: TileEntity? -> t!!.value == TileValue.NONE }
                .count() == 1L
    }

    /**
     * @param value tile value
     * @return true if this combo has 1 of value and 2 empty slots
     */
    fun isOneThird(value: TileValue?): Boolean {
        val oppositeValue = if (value == TileValue.X) TileValue.O else TileValue.X
        return if (tiles!!.stream().anyMatch { t: TileEntity? -> t!!.value == oppositeValue }) false else tiles.stream()
                .filter { t: TileEntity? -> t!!.value == TileValue.NONE }
                .count() == 2L
    }

    /**
     * @return first empty tile or null if no empty tiles
     */
    val firstEmpty: TileEntity?
        get() = tiles!!.stream()
                .filter { t: TileEntity? -> t!!.value == TileValue.NONE }
                .findAny()
                .orElse(null)

    val winSymbol: String?
        get() = tile1!!.value.toString() // GKMOD - get() is looking for a string, not a symbol   .value.symbol

    init {
        tiles = Arrays.asList(tile1, tile2, tile3)
    }
}