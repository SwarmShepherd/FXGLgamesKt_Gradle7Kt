package com.almasb.fxglgames.tictactoe.components.enemy

import com.almasb.fxglgames.tictactoe.TileValue
import java.util.*
import com.almasb.fxglgames.tictactoe.TicTacToeApp as TicTacToeApp


/**
 *      “A heuristic technique, often called simply a heuristic,
 *      is any approach to problem solving, learning, or discovery that employs a
 *      practical method not guaranteed to be optimal or perfect, but sufficient
 *      for the immediate goals.
 *
 */


/**
 * The minimax algorithm is adapted from
 * https://www.ntu.edu.sg/home/ehchua/programming/java/JavaGame_TicTacToe_AI.html
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class MinimaxComponent : EnemyComponent() {
    private val mySeed  = TileValue.O
    private val oppSeed = TileValue.X
    private val cells = Array(3) { arrayOfNulls<TileValue?>(3) }

    /** The heuristic* evaluation function for the given line of 3 cells
     * @Return +100, +10, +1 for 3-, 2-, 1-in-a-line for computer.
     * -100, -10, -1 for 3-, 2-, 1-in-a-line for opponent.
     * 0 otherwise
     *
     */
    private fun _evaluateLine(row1: Int, col1: Int, row2: Int, col2: Int, row3: Int, col3: Int): Int {
        var score = 0

        // First cell
        if (cells[row1][col1] === mySeed) {
            score = 1
        }
        else if (cells[row1][col1] === oppSeed) {// Second cell
            score = -1
        }

        //GKNOTE: Center?
        if (cells[row2][col2] === mySeed) {

            score = if (score == 1) {   // cell1 is mySeed
                10
            } else if (score == -1) {  // cell1 is oppSeed
                return 0
            } else {  // cell1 is empty
                1
            }

        }
        else if (cells[row2][col2] === oppSeed) {// Third cell
            score = if (score == -1) { // cell1 is oppSeed
                -10
            } else if (score == 1) { // cell1 is mySeed
                return 0
            } else {  // cell1 is empty
                -1
            }
        }

        if (cells[row3][col3] === mySeed) {
            if (score > 0) {  // cell1 and/or cell2 is mySeed
                score *= 10
            } else if (score < 0) {  // cell1 and/or cell2 is oppSeed
                return 0
            } else {  // cell1 and cell2 are empty
                score = 1
            }
        } else if (cells[row3][col3] === oppSeed) {
            if (score < 0) {  // cell1 and/or cell2 is oppSeed
                score *= 10
            } else if (score > 1) {  // cell1 and/or cell2 is mySeed
                return 0
            } else {  // cell1 and cell2 are empty
                score = -1
            }
        }
        return score
    }

    private val _winningPatterns = intArrayOf(
            448, 56, 7,    // rows
            292, 146, 73,  // cols
            273, 84        // diagonals
    )

    /** Returns true if thePlayer wins  */
    private fun _hasWon(thePlayer: TileValue): Boolean {
        var pattern = 0  // 9-bit pattern for the 9 cells

        for (row in 0..2) {
            for (col in 0..2) {
                if (cells[row][col] === thePlayer) {
                    pattern = pattern or (1 shl row * 3 + col)
                }
            }
        }
        for (winningPattern in _winningPatterns) {
            if (pattern and winningPattern == winningPattern) return true
        }
        return false
    }

    /** Find all valid next moves.
     * Return List of moves in int[2] of {row, col} or empty list if gameover  */
    private fun _generateMoves(): List<IntArray> {
        val nextMoves: MutableList<IntArray> = ArrayList() // allocate List

        // If gameover, i.e., no next move
        if (_hasWon(mySeed) || _hasWon(oppSeed)) {
            return nextMoves   // return empty list
        }

        // Search for empty cells and add to the List
        for (row in 0..2) {
            for (col in 0..2) {
                if (cells[row][col] === TileValue.NONE) {
                    nextMoves.add(intArrayOf(row, col))
                }
            }
        }
        return nextMoves
    }

    /** The heuristic* evaluation function for the current board
     * @Return +100, +10, +1 for EACH 3-, 2-, 1-in-a-line for computer.
     * -100, -10, -1 for EACH 3-, 2-, 1-in-a-line for opponent.
     * 0 otherwise
     */
    private fun _evaluate(): Int {
        var score = 0
        // _evaluateLine score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)

        score += _evaluateLine(0, 0, 0, 1, 0, 2)
        score += _evaluateLine(1, 0, 1, 1, 1, 2)
        score += _evaluateLine(2, 0, 2, 1, 2, 2)
        score += _evaluateLine(0, 0, 1, 0, 2, 0)
        score += _evaluateLine(0, 1, 1, 1, 2, 1)
        score += _evaluateLine(0, 2, 1, 2, 2, 2)
        score += _evaluateLine(0, 0, 1, 1, 2, 2)
        score += _evaluateLine(0, 2, 1, 1, 2, 0)
        return score
    }

    /** Recursive minimax at level of depth for either maximizing or minimizing player.
     * Return int[3] of {score, row, col}   */
    private fun _minimax(depth: Int, player: TileValue): IntArray {
        // Generate possible next moves in a List of int[2] of {row, col}.
        val nextMoves = _generateMoves()

        // mySeed is maximizing; while oppSeed is minimizing
        var bestScore = if (player === mySeed) Integer.MIN_VALUE else Integer.MAX_VALUE
        var currentScore: Int
        var bestRow = -1
        var bestCol = -1
        if (nextMoves.isEmpty() || depth == 0) {
            // Gameover or depth reached, evaluate score
            bestScore = _evaluate()
        } else {
            for (move in nextMoves) {
                // Try this move for the current "player"
                cells[move[0]][move[1]] = player
                if (player === mySeed) {  // mySeed (computer) is maximizing player
                    currentScore = _minimax(depth - 1, oppSeed)[0]
                    if (currentScore > bestScore) {
                        bestScore = currentScore
                        bestRow = move[0]
                        bestCol = move[1]
                    }
                }
                // Undo move
                else {  // oppSeed is minimizing player
                    currentScore = _minimax(depth - 1, mySeed)[0]
                    if (currentScore < bestScore) {
                        bestScore = currentScore
                        bestRow = move[0]
                        bestCol = move[1]
                    }
                }
                cells[move[0]][move[1]] = TileValue.NONE
            }
        }
        return intArrayOf(bestScore, bestRow, bestCol)
    }

    //========================================================================================

    override fun makeMove() {
        //GKMOD: Put board + combos into Companion Object - squelching compiler anyway...
        //var board   = TicTacToeApp.board  //THIS WAS WORKING...
        val board  = TicTacToeApp.board

        // the algorithm uses [row][col]
        for (y in 0..2) {
            for (x in 0..2) {
                cells[y][x] = board[x][y]?.value   //GKMOD - 'forced' to add '?' without knowing
            }
        }
        val result = _minimax(2, mySeed) // depth, max turn

        board[result[2]][result[1]]?.control?.mark(mySeed)    //GKMOD -'forced' to add '?' without knowing
    }

}