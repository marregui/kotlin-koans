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

package kotlinkoans.algorithms

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class QSortTest {

    @Test
    fun test_qsort() {
        val array = intArrayOf(1, 2, 3, 4, 5).toTypedArray()
        qsort(array)
        assertThat(array, `is`(intArrayOf(5, 4, 3, 2, 1)))
    }

    @Test
    fun test_qsort_randomized() {
        val array = generateRandArray(10, 10)
        val arrayCopy = array.copyOf()
        qsort(array)
        Arrays.sort(arrayCopy)
        arrayCopy.reverse()
        assertThat(array, `is`(arrayCopy))
    }

    private fun generateRandArray(size: Int, max: Int): Array<Int> {
        val r = ThreadLocalRandom.current()
        return Array(size) { r.nextInt(max + 1) }
    }
}
