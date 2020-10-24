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
import java.awt.event.KeyListener
import java.util.*
import javax.swing.JPanel
import javax.swing.SwingUtilities

abstract class Game : JPanel(BorderLayout()), KeyListener {
    protected var timer: Timer? = null // null means the game has not started
    protected var heartBeat = 70L

    protected abstract fun updateGame()

    protected abstract fun drawGame(g2: Graphics2D)

    final override fun paintComponent(g: Graphics) {
        drawGame(g as Graphics2D)
    }

    protected fun startHeartbeat() {
        timer = Timer(true)
        timer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                updateGame()
                SwingUtilities.invokeLater { repaint() }
            }
        }, heartBeat, heartBeat)
    }

    protected fun stopHeartbeat() {
        timer?.cancel()
        timer = null
    }

    override fun keyPressed(e: KeyEvent) {
        /* no-op */
    }

    override fun keyTyped(e: KeyEvent) {
        /* no-op */
    }

    override fun keyReleased(e: KeyEvent) {
        /* no-op */
    }

    companion object {
        val TEXT_COLOR: Color = Color.WHITE
        val TEXT_FONT = Font("TimesRoman", Font.BOLD, 18)
    }
}
