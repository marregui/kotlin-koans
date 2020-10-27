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

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.jvm.Throws

/**
 * Simplified implementation of a linked concurrent blocking queue, of
 * maximum capacity for put/take operations (the most habitual calls).
 * All operations are atomic and thread safe.
 */
class BlockingQueue<T>(val capacity: Int) {

    private val size = AtomicInteger(0)
    private val headLock = ReentrantLock()
    private val tailLock = ReentrantLock()
    private val awaitForSpace: Condition = tailLock.newCondition()
    private val atLeastOneItem: Condition = headLock.newCondition()
    private lateinit var head: Node<T>
    private lateinit var tail: Node<T>

    init {
        head = tail.also { tail = head }
    }

    /**
     * Puts the item at the end of the queue. Will block if the queue is full.
     */
    @Throws(InterruptedException::class)
    fun put(item: T) {
        val node = Node(item)
        tailLock.lockInterruptibly()
        try {
            while (size() == capacity) {
                awaitForSpace.await()
            }
            tail.next = node
            tail = node
            if (size.incrementAndGet() < capacity) {
                awaitForSpace.signal()
            }
        } finally {
            tailLock.unlock()
        }
        if (size() > 0) {
            headLock.lock()
            try {
                atLeastOneItem.signal()
            } finally {
                headLock.unlock()
            }
        }
    }

    /**
     * The FIFO element, at the head, blocking if the queue is empty
     */
    fun take(): T {
        var item: T
        headLock.lockInterruptibly()
        try {
            while (size.get() == 0) {
                atLeastOneItem.await()
            }
            item = head.item
            head = head.next!!
            if (size.decrementAndGet() > 0) {
                atLeastOneItem.signal()
            }
        } finally {
            headLock.unlock()
        }
        if (size() < capacity) {
            tailLock.lock()
            try {
                awaitForSpace.signal()
            } finally {
                tailLock.unlock()
            }
        }
        return item
    }

    fun clear() {
        tailLock.lock()
        headLock.lock()
        try {
            head.next = null
            tail.next = null
            tail = head
            size.set(0)
            awaitForSpace.signal()
        } finally {
            tailLock.unlock()
            headLock.unlock()
        }
    }

    fun dump() {
        tailLock.lock()
        headLock.lock()
        try {
            var i = 0
            LOGGER.info("Head: ${head.item} (-> ${head.next})")
            LOGGER.info("Tail: ${tail.item} (-> ${tail.next})")
            LOGGER.info("Head == Tail: ${head === tail}")
            var n: Node<T>? = head
            while (n != null) {
                LOGGER.info(" - Node ${i++}: ${n.item}${if (n === head) "(Head)" else if (n === tail) "(Tail)" else "(Node)"}")
                n = n.next
            }
        } finally {
            tailLock.unlock()
            headLock.unlock()
        }
    }

    fun size(): Int {
        return size.get()
    }

    val isEmpty: Boolean
        get() = size() == 0

    val isFull: Boolean
        get() = size() >= capacity

    companion object {
        private data class Node<T>(var item: T, var next: Node<T>? = null)

        private val LOGGER: Logger = LoggerFactory.getLogger(BlockingQueue::class.java)
    }
}
