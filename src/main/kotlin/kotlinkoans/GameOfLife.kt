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
import java.awt.event.KeyEvent
import java.awt.geom.Ellipse2D
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import javax.swing.JFrame
import kotlin.math.log10

class GameOfLife(private val n: Int) : Game() {
    // grid[offset] in a 2D array would be grid[offset / n][offset % n]
    private var grid = BooleanArray(n * n)
    // double buffer for rendering.
    private var swapGrid = BooleanArray(n * n)
    private var statusMsg = "Space to pause/resume, Enter to pause/reset"

    init {
        isDoubleBuffered = true
        createFirstGeneration()
        startHeartbeat()
    }

    override fun keyPressed(e: KeyEvent) {
        when (e.keyCode) {
            KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> if (timer === null) {
                if (e.keyCode == KeyEvent.VK_ENTER) {
                    createFirstGeneration()
                }
                startHeartbeat()
            } else {
                stopHeartbeat()
            }
        }
    }

    private fun createFirstGeneration() {
        Arrays.fill(grid, false)
        with(ThreadLocalRandom.current()) {
            for (p in 0 until log10(n.toDouble()).toInt()) {
                val a = nextInt(n)
                val b = nextInt(n)
                for (i in a.coerceAtMost(b) until a.coerceAtLeast(b)) {
                    val c = nextInt(n)
                    val d = nextInt(n)
                    for (j in c.coerceAtMost(d) until c.coerceAtLeast(d)) {
                        grid[i * n + j] = ((p + 1) * nextInt(n)) % 17 == 0
                    }
                }
            }
        }
    }

    private fun liveCount(offset: Int): Int {
        val coordRange = 0 until n
        var count = 0
        val i = offset / n
        val j = offset % n
        for (x in (i - 1)..(i + 1)) {
            for (y in (j - 1)..(j + 2)) {
                if (x in coordRange && y in coordRange && x != j && y != i && grid[x * n + y]) {
                    count++
                }
            }
        }
        return count
    }

    override fun updateGame() {
        for (i in grid.indices) {
            val liveCnt = liveCount(i)
            swapGrid[i] = if (grid[i]) liveCnt in 2..3 else liveCnt == 3
        }
        grid = swapGrid.also { swapGrid = grid }
    }

    override fun drawGame(g2: Graphics2D) {
        with(g2) {
            setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED)
            setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED)
            val yDelta = height / n.toDouble()
            val xDelta = width / n.toDouble()
            color = BG_COLOR
            fillRect(0, 0, width, height)
            if (!statusMsg.isBlank()) {
                color = TEXT_COLOR
                font = TEXT_FONT
                drawString(statusMsg, 10, 10 + g2.fontMetrics.height)
            }
            color = CELL_COLOR
            for (i in grid.indices) {
                if (grid[i]) {
                    fill(Ellipse2D.Double(i / n * xDelta, i % n * yDelta, xDelta, yDelta))
                }
            }
        }
    }

    companion object {
        private val BG_COLOR = Color.BLACK
        private val CELL_COLOR = Color.CYAN

        @JvmStatic
        fun main(args: Array<String>) {
            val width = 900
            val height = 800
            val game = GameOfLife(122)
//            val game = GameOfLife(421)
            with(JFrame("Game Of Life V1.0")) {
                defaultCloseOperation = JFrame.EXIT_ON_CLOSE
                contentPane.add(game)
                addKeyListener(game)
                setSize(width, height)
                centerInScreen()
            }
        }
    }
}
