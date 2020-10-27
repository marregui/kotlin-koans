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

inline fun <reified T : Any> newHeap(noinline condition: (T, T) -> Boolean) =
    Heap(arrayOfNulls<T>(Heap.INIT_SIZE), condition)

class Heap<T>(var delegate: Array<T?>, val heapCondition: (T, T) -> Boolean) {
    private var insertIdx = 0

    val size: Int
        get() = insertIdx

    fun peek() = if (insertIdx > 0) delegate[0] else null

    private fun pushAndShiftDown(value: T) {
        delegate[insertIdx] = value
        var i = insertIdx++
        var parent = parent(i)
        while (i > 0 && !heapCondition(delegate[parent]!!, delegate[i]!!)) {
            delegate[i] = delegate[parent].also { delegate[parent] = delegate[i] }
            i = parent
            parent = parent(i)
        }
    }

    fun push(vararg next: T): Heap<T> {
        if (insertIdx + next.size >= delegate.size) {
            delegate = delegate.copyOf((2 * delegate.size).coerceAtLeast(insertIdx + next.size))
        }
        next.forEach(::pushAndShiftDown)
        return this
    }

    inline fun <reified R : T> popAll(): Array<R>? = if (size == 0) null else Array(size) { pop()!! as R }

    fun pop(): T? {
        if (insertIdx == 0) {
            return null
        }
        val rootValue = delegate[0]
        insertIdx--
        delegate[0] = delegate[insertIdx].also { delegate[insertIdx] = null }
        if (insertIdx > 1) {
            var i = 0
            while (true) {
                val left = left(i)
                val right = right(i)
                val value = delegate[i]!!
                if (right < insertIdx) { // we have two children
                    val leftValue = delegate[left]!!
                    val rightValue = delegate[right]!!
                    if (heapCondition(value as T, leftValue as T) && heapCondition(value, rightValue as T)) {
                        break
                    }
                    val (j, k) = if (heapCondition(rightValue as T, leftValue as T)) right to left else left to right
                    if (!heapCondition(value, delegate[j]!! as T)) {
                        delegate[i] = delegate[j].also { delegate[j] = delegate[i] }
                        i = j
                    } else if (!heapCondition(value, delegate[k]!! as T)) {
                        delegate[i] = delegate[k].also { delegate[k] = delegate[i] }
                        i = k
                    }
                } else if (left < insertIdx) { // we only have the left child
                    if (heapCondition(value as T, delegate[left]!! as T)) {
                        break
                    }
                    if (!heapCondition(value, delegate[left]!! as T)) {
                        delegate[i] = delegate[left].also { delegate[left] = delegate[i] }
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
        delegate = delegate.copyOf(INIT_SIZE)
        Arrays.fill(delegate, null)
        insertIdx = 0
    }

    companion object {
        const val INIT_SIZE = 10
        private fun parent(i: Int) = (i - 1) / 2
        private fun left(i: Int) = 2 * i + 1
        private fun right(i: Int) = 2 * i + 2
    }
}
