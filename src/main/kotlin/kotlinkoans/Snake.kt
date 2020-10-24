/*
 * Licensed to Miguel Arregui ("marregui") under one or more contributor
 * license agreements. See the LICENSE file distributed with this work
 * for additional information regarding copyright ownership. You may
 * obtain a copy at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * Copyright 2020, Miguel Arregui a.k.a. marregui
 */

package kotlinkoans

import java.awt.*
import java.awt.Font
import java.awt.event.KeyEvent
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import javax.swing.JFrame
import kotlin.math.ceil

class Snake(private val n: Int, private val drawGrid: Boolean = false) : Game() {
    private val snake: LinkedList<Coord> = LinkedList<Coord>()
    private val startLen = n / 5
    private var direction = DOWN
    private var apple: Coord? = null
    private var statusMsg = ""

    init {
        heartBeat = HEARTBEAT_START
    }

    override fun keyPressed(e: KeyEvent) {
        when (e.keyCode) {
            KeyEvent.VK_UP, KeyEvent.VK_W -> if (direction != DOWN) direction = UP
            KeyEvent.VK_DOWN, KeyEvent.VK_S -> if (direction != UP) direction = DOWN
            KeyEvent.VK_LEFT, KeyEvent.VK_A -> if (direction != RIGHT) direction = LEFT
            KeyEvent.VK_RIGHT, KeyEvent.VK_D -> if (direction != LEFT) direction = RIGHT
            KeyEvent.VK_SPACE -> if (timer === null) {
                statusMsg = ""
                snake.clear()
                (0 until startLen).map { Coord(0, startLen - (it + 1), n) }.forEach(snake::add)
                apple = Coord.randCoord(n)
                direction = DOWN
                heartBeat = HEARTBEAT_START
                startHeartbeat()
            }
        }
    }

    override fun updateGame() {
        val head = snake[0] + direction
        if (head.isOutOfBoard() || head in snake.subList(1, snake.size)) {
            statusMsg = "you lost!"
            stopHeartbeat()
        } else {
            snake.addFirst(head)
            if (head == apple) {
                if (snake.size == n * 2) {
                    statusMsg = "you won!!!"
                    stopHeartbeat()
                } else {
                    apple = Coord.randCoord(n)
                    heartBeat = ceil(heartBeat * 0.95).toLong().coerceAtLeast(HEARTBEAT_LOWEST)
                    stopHeartbeat()
                    startHeartbeat()
                }
            } else {
                snake.removeLast()
            }
        }
    }

    override fun drawGame(g2: Graphics2D) {
        val topLeftX = MARGIN_WIDTH
        val topLeftY = HEADER_HEIGHT + MARGIN_WIDTH
        val boardWidth = width - 2 * MARGIN_WIDTH
        val boardHeight = height - HEADER_HEIGHT - 2 * MARGIN_WIDTH
        val xDelta = boardWidth / n.toDouble()
        val yDelta = boardHeight / n.toDouble()
        with(g2) {
            setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
            setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
            color = CANVAS_COLOR
            fillRect(0, 0, width, height)
            color = TEXT_COLOR
            font = TEXT_FONT
            var scoreTxt = "Score: ${if (snake.size > 0) snake.size - startLen else 0}"
            if (!statusMsg.isBlank()) {
                scoreTxt += " $statusMsg"
            }
            drawString(scoreTxt, MARGIN_WIDTH, HEADER_HEIGHT + MARGIN_WIDTH / 2)
            color = GRID_COLOR
            drawRect(topLeftX, topLeftY, boardWidth, boardHeight)
            if (drawGrid) {
                for (i in 1 until n) {
                    val x = topLeftX + (xDelta * i).ceilToInt()
                    val y = topLeftY + (yDelta * i).ceilToInt()
                    drawLine(topLeftX, y, topLeftX + boardWidth, y)
                    drawLine(x, topLeftY, x, topLeftY + boardHeight)
                }
            }
            color = APPLE_COLOR
            val ovalWidth = (xDelta / 2).ceilToInt()
            val ovalHeight = (yDelta / 2).ceilToInt()
            if (apple != null) {
                fillOval(
                    apple!!.xInBoard(topLeftX, xDelta, ovalWidth),
                    apple!!.yInBoard(topLeftY, yDelta, ovalHeight),
                    ovalWidth, ovalHeight
                )
            }
            if (snake.size > 0) {
                color = SNAKE_BODY_COLOR
                for (i in 1 until snake.size) {
                    fillOval(
                        snake[i].xInBoard(topLeftX, xDelta, ovalWidth),
                        snake[i].yInBoard(topLeftY, yDelta, ovalHeight),
                        ovalWidth, ovalHeight
                    )
                }
                color = SNAKE_HEAD_COLOR
                fillOval(
                    snake[0].xInBoard(topLeftX, xDelta, ovalWidth),
                    snake[0].yInBoard(topLeftY, yDelta, ovalHeight),
                    ovalWidth, ovalHeight
                )
            }
            if (timer === null) {
                val text = """
                        | Up, Down, Left, Right arrows to move,
                        | press space to start... """.trimMargin()
                val textX = topLeftX + boardWidth / 5
                val textY = topLeftY + boardHeight / 2
                if (drawGrid) {
                    color = CANVAS_COLOR
                    val textWidth = g2.fontMetrics.stringWidth(text)
                    val textHeight = g2.fontMetrics.height * 2
                    fillRect(
                        textX - MARGIN_WIDTH,
                        textY - textHeight,
                        textWidth + MARGIN_WIDTH,
                        textHeight * 2
                    )
                }
                color = TEXT_COLOR
                drawString(text, textX, textY + ovalHeight)
            }
        }
    }

    companion object {
        val UP = Coord(0, -1, 0)
        val DOWN = Coord(0, 1, 0)
        val LEFT = Coord(-1, 0, 0)
        val RIGHT = Coord(1, 0, 0)
        const val HEADER_HEIGHT = 25
        const val MARGIN_WIDTH = 30
        val CANVAS_COLOR: Color = Color.BLACK
        val GRID_COLOR: Color = Color.GREEN.brighter().brighter()
        val APPLE_COLOR: Color = Color.RED
        val SNAKE_HEAD_COLOR: Color = Color.BLUE
        val SNAKE_BODY_COLOR: Color = Color.YELLOW
        const val HEARTBEAT_LOWEST = 50L // full speed
        const val HEARTBEAT_START = 140L

        data class Coord(val x: Int, val y: Int, val n: Int) {
            operator fun plus(other: Coord): Coord = Coord(x + other.x, y + other.y, n)
            fun isOutOfBoard() = x < 0 || x >= n || y < 0 || y >= n
            fun xInBoard(topLeft: Int, delta: Double, width: Int) = topLeft + (delta * x + width / 2).ceilToInt()
            fun yInBoard(topLeft: Int, delta: Double, height: Int) = topLeft + (delta * y + height / 2).ceilToInt()

            companion object {
                fun randCoord(n: Int): Coord {
                    val r = ThreadLocalRandom.current()
                    return Coord(r.nextInt(n), r.nextInt(n), n)
                }
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val width = 900
            val height = 800
            val game = Snake(32, drawGrid = false) // 32 * 32 grid
            with(JFrame("Snake V1.0")) {
                defaultCloseOperation = JFrame.EXIT_ON_CLOSE
                contentPane.add(game)
                addKeyListener(game)
                setSize(width, height)
                centerInScreen()
            }
        }
    }
}
