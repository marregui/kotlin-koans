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

sealed class Heap(val heapCondition: (Int, Int) -> Boolean) {
    private var array = Array<Int?>(INIT_SIZE) { null }
    private var insertIdx = 0

    @Synchronized
    fun push(value: Int) {
        if (insertIdx >= array.size) {
            array = array.copyOf(2 * INIT_SIZE)
        }
        array[insertIdx] = value
        var i = insertIdx++
        var parent = parent(i)
        while (i >= 0 && !heapCondition(array[parent]!!, array[i]!!)) {
            i = swap(i, parent)
            parent = parent(i)
        }
    }

    @Synchronized
    fun pop(): Int? {
        if (insertIdx == 0) {
            return null
        }
        val rootValue = array[0]
        insertIdx--
        array[0] = array[insertIdx].also { array[insertIdx] = null }
        if (insertIdx > 1) {
            var i = 0
            while (true) {
                val left = left(i)
                val right = right(i)
                val value = array[i]!!
                if (right < insertIdx) { // we have two children
                    val leftValue = array[left]!!
                    val rightValue = array[right]!!
                    if (heapCondition(value, leftValue) && heapCondition(value, rightValue)) {
                        break
                    }
                    val (j, k) = if (heapCondition(rightValue, leftValue)) right to left else left to right
                    if (!heapCondition(value, array[j]!!)) {
                        i = swap(i, j)
                    } else if (!heapCondition(value, array[k]!!)) {
                        i = swap(i, k)
                    }
                } else if (left < insertIdx) { // we only have the left child
                    if (heapCondition(value, array[left]!!)) {
                        break
                    }
                    if (!heapCondition(value, array[left]!!)) {
                        i = swap(i, left)
                    }
                } else { // no children
                    break
                }
            }
        }
        return rootValue
    }

    @Synchronized
    fun peek() = if (insertIdx > 0) array[0] else null

    @Synchronized
    fun size() = insertIdx

    private fun swap(i: Int, j: Int): Int {
        array[j] = array[i].also { array[i] = array[j] }
        return j
    }

    companion object {
        const val INIT_SIZE = 10
        fun parent(i: Int) = (i - 1) / 2
        fun left(i: Int) = 2 * i + 1
        fun right(i: Int) = 2 * i + 2
        private class MinHeap : Heap({ a, b -> a <= b})
        private class MaxHeap: Heap({ a, b -> a >= b})
        fun min(): Heap = MinHeap()
        fun max(): Heap = MaxHeap()
    }
}

fun main() {
    for (heap in listOf(Heap.min(), Heap.max())) {
        arrayOf(2, 7, 11, 15, -37, -1, 2, 0, 3, 1, 2, 1).forEach(heap::push)
        (0 until heap.size()).forEach { _ ->  print(" ${heap.pop()}") }.also { println() }
    }
}
