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

import java.util.*

sealed class Heap<T>(val heapCondition: (T, T) -> Boolean) {
    private var array = arrayOfNulls<Any?>(INIT_SIZE)
    private var insertIdx = 0

    val size: Int
        get() = insertIdx

    fun peek() = if (insertIdx > 0) array[0] else null

    private inline fun pushAndShiftDown(value: T) {
        array[insertIdx] = value
        var i = insertIdx++
        var parent = parent(i)
        while (i > 0 && !heapCondition(array[parent]!! as T, array[i]!! as T)) {
            array[i] = array[parent].also { array[parent] = array[i] }
            i = parent
            parent = parent(i)
        }
    }

    fun push(a: Array<T>): Heap<T> {
        if (insertIdx + a.size >= array.size) {
            array = array.copyOf((2 * array.size).coerceAtLeast(insertIdx + a.size))
        }
        a.forEach(::pushAndShiftDown)
        return this
    }

    fun push(first: T, vararg next: T): Heap<T> {
        if (insertIdx + next.size >= array.size) {
            array = array.copyOf((2 * array.size).coerceAtLeast(insertIdx + next.size))
        }
        pushAndShiftDown(first)
        next.forEach(::pushAndShiftDown)
        return this
    }

    inline fun <reified R: T> popAll(): Array<R>? = if (size == 0) null else Array(size) { pop()!! as R}

    fun pop(): T? {
        if (insertIdx == 0) {
            return null
        }
        val rootValue = array[0] as T
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
                    if (heapCondition(value as T, leftValue as T) && heapCondition(value, rightValue as T)) {
                        break
                    }
                    val (j, k) = if (heapCondition(rightValue as T, leftValue as T)) right to left else left to right
                    if (!heapCondition(value, array[j]!! as T)) {
                        array[i] = array[j].also { array[j] = array[i] }
                        i = j
                    } else if (!heapCondition(value, array[k]!! as T)) {
                        array[i] = array[k].also { array[k] = array[i] }
                        i = k
                    }
                } else if (left < insertIdx) { // we only have the left child
                    if (heapCondition(value as T, array[left]!! as T)) {
                        break
                    }
                    if (!heapCondition(value, array[left]!! as T)) {
                        array[i] = array[left].also { array[left] = array[i] }
                        i = left
                    }
                } else { // no children
                    break
                }
            }
        }
        return rootValue
    }

    fun clean() {
        array = array.copyOf(INIT_SIZE)
        Arrays.fill(array, null)
        insertIdx = 0
    }

    companion object {
        const val INIT_SIZE = 10
        fun parent(i: Int) = (i - 1) / 2
        fun left(i: Int) = 2 * i + 1
        fun right(i: Int) = 2 * i + 2

        private class HeapImpl<T>(heapCondition: (T, T) -> Boolean) : Heap<T>(heapCondition)

        fun <T> newInstance(heapCondition: (T, T) -> Boolean): Heap<T> = HeapImpl(heapCondition)
    }
}
