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

class HeapSortTest {

    @Test
    fun test_heap_sort_int_array() {
        val array = arrayOf(2, 7, 11, 15, -37, -1, 2, 0, 3, 1, 2, 1)
        assertThat(heapSortedOf(array) { a, b -> a < b},
            `is`(arrayOf(-37, -1, 0, 1, 1, 2, 2, 2, 3, 7, 11, 15)))
        assertThat(heapSortedOf(array) { a, b -> a > b},
            `is`(arrayOf(15, 11, 7, 3, 2, 2, 2, 1, 1, 0, -1, -37)))
    }

    @Test
    fun test_heap_sort_string_array() {
        val array = arrayOf("Miguel", "Pablo",  "Pedro", "Catalina")
        assertThat(heapSortedOf(array){ a, b -> a < b },
            `is`(arrayOf("Catalina", "Miguel", "Pablo", "Pedro")))
        assertThat(heapSortedOf(array){ a, b -> a > b },
            `is`(arrayOf("Pedro", "Pablo", "Miguel", "Catalina")))
    }
}
