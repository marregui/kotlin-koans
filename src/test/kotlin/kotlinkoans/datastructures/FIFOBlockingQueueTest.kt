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
import org.hamcrest.Matchers.greaterThan
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit

class BlockingQueueTest {

    @Test
    fun test_min_heap_array_parameter() {
        val blockDelay = 200L
        val queue = BlockingQueue<Int>(1)
        val executor = Executors.newSingleThreadExecutor(ThreadFactory {
            val t = Thread(it)
            t.name = "unblocker"
            t.isDaemon = true
            t
        })
        executor.submit {
            TimeUnit.MILLISECONDS.sleep(blockDelay)
            assertThat(queue.take(), `is`(1)) // unblock
        }
        queue.put(1)
        val start = System.nanoTime()
        queue.put(2) // block
        val elapsed = ((System.nanoTime() - start) / 1000_000).toInt()
        assertThat(elapsed, greaterThan(blockDelay.toInt()))
        assertThat(queue.take(), `is`(2))
        assertThat(queue.size(), `is`(0))
    }
}
