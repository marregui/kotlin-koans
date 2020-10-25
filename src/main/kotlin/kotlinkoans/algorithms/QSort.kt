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

fun <T: Comparable<T>> qsort(array: Array<T>) {
    val stack = IndexStack()
    stack.push(0, array.size - 1)
    while (stack.size > 0) {
        val (start, end) = stack.pop()
        var p = start
        for (i in p until end) {
            if (array[i] > array[end]) {
                if (i != p) {
                    array[p] = array[i].also { array[i] = array[p] }
                }
                p++
            }
        }
        array[p] = array[end].also { array[end] = array[p] }
        if (p - 1 > start) {
            stack.push(start, p - 1) // there are elements left of pivot, push left
        }
        if (p + 1 < end) {
            stack.push(p + 1, end) // there are elements right of pivot, push right
        }
    }
}

private class IndexStack {
    private var stack = Array<Pair<Int, Int>?>(5) { null }
    private var insertIdx = 0

    fun push(start: Int, end: Int) {
        if (insertIdx >= stack.size) {
            stack = stack.copyOf(stack.size * 2)
        }
        stack[insertIdx++] = Pair(start, end)
    }

    fun pop(): Pair<Int, Int> {
        if (insertIdx <= 0) {
            throw IllegalStateException("stack is empty")
        }
        return stack[--insertIdx]!!.also { stack[insertIdx] = null }
    }

    val size: Int
        get() = insertIdx
}
