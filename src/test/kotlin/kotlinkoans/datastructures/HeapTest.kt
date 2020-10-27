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

package kotlinkoans.datastructures

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HeapTest {

    lateinit var array: Array<Int>
    private val minIntHeap = newHeap<Int>{ a, b -> a < b}
    private val maxIntHeap = newHeap<Int>{ a, b -> a > b}

    @BeforeEach
    fun beforeEach() {
        array = arrayOf(2, 7, 11, 15, -37, -1, 2, 0, 3, 1, 2, 1)
    }

    @AfterEach
    fun afterEach() {
        minIntHeap.clean()
        maxIntHeap.clean()
    }

    @Test
    fun test_min_heap_array_parameter() {
        assertThat(
            minIntHeap.push(*array).popAll()!!,
            `is`(arrayOf(-37, -1, 0, 1, 1, 2, 2, 2, 3, 7, 11, 15)))
        assertThat(minIntHeap.size, `is`(0))
    }

    @Test
    fun test_max_heap_array_parameter() {
        assertThat(
            maxIntHeap.push(*array).popAll()!!,
            `is`(arrayOf(15, 11, 7, 3, 2, 2, 2, 1, 1, 0, -1, -37)))
        assertThat(maxIntHeap.size, `is`(0))
    }

    @Test
    fun test_min_heap_vararg_parameters() {
        assertThat(
            minIntHeap.push(2, 7, 11, 15, -37, -1, 2, 0, 3, 1, 2, 1).popAll()!!,
            `is`(arrayOf(-37, -1, 0, 1, 1, 2, 2, 2, 3, 7, 11, 15)))
        assertThat(minIntHeap.size, `is`(0))
    }

    @Test
    fun test_max_heap_vararg_parameters() {
        assertThat(
            maxIntHeap.push(2, 7, 11, 15, -37, -1, 2, 0, 3, 1, 2, 1).popAll()!!,
            `is`(arrayOf(15, 11, 7, 3, 2, 2, 2, 1, 1, 0, -1, -37)))
        assertThat(maxIntHeap.size, `is`(0))
    }

    @Test
    fun test_min_heap_string_parameters() {
        val array = arrayOf("Miguel", "Pablo",  "Pedro", "Catalina")
        val minHeap = newHeap<String>{ a, b -> a < b}
        assertThat(
            minHeap.push(*array).popAll()!!,
            `is`(arrayOf("Catalina", "Miguel", "Pablo", "Pedro")))
        assertThat(minHeap.size, `is`(0))
    }

    @Test
    fun test_max_heap_string_vararg_parameters() {
        val maxHeap = newHeap<String>{ a, b -> a > b}
        assertThat(
            maxHeap.push("Miguel", "Pablo",  "Pedro", "Catalina").popAll()!!,
            `is`(arrayOf("Pedro", "Pablo", "Miguel", "Catalina")))
    }
}
