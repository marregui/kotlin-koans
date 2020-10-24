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

import java.awt.Toolkit
import javax.swing.JFrame
import kotlin.math.ceil

fun JFrame.centerInScreen() {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val x = (screenSize.width - width) / 2
    val y = (screenSize.height - height) / 2
    setLocation(x, y);
    isVisible = true
}

fun Double.ceilToInt(): Int = ceil(this).toInt()