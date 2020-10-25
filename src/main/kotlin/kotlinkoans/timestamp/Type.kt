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

package kotlinkoans.timestamp

import java.lang.reflect.Modifier
import java.util.*
import java.util.function.Supplier

abstract class Type protected constructor(val name: String, valueSupplier: Supplier<*>? = null) : MutableIterator<Any> {

    @Volatile
    private var valueSupplier: Supplier<*>? = valueSupplier

    fun withValueSupplier(valueSupplier: Supplier<*>?): Type {
        this.valueSupplier = valueSupplier
        return this
    }

    override fun next(): Any = valueSupplier?.get() ?: throw NoSuchElementException("null valueSupplier $name")

    override fun hasNext(): Boolean = valueSupplier != null

    override fun remove() = Unit

    override fun toString(): String {
        val sb = StringBuilder(javaClass.simpleName).append("{")
        val len = sb.length
        for (field in javaClass.declaredFields) {
            field.isAccessible = true
            val m = field.modifiers
            if (Modifier.isPrivate(m) && Modifier.isFinal(m) && !Modifier.isStatic(m)) {
                sb.append(field.name).append("=").append(field[this]).append(", ")
            }
        }
        if (sb.length > len) {
            sb.setLength(sb.length - 2) // remove trailing comma
        }
        sb.append("}")
        return sb.toString()
    }

    override fun hashCode() = name.hashCode()

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        return o is Type && name == o.name
    }
}