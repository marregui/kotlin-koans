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

class Timestamp(dt: String?) : Type("timestamp") {
    val utcEpochMicros: Long = TimestampParser.parse(dt) // if dt == null -> current system timestamp
    val asStr: String = TimestampParser.format(utcEpochMicros)

    init {
        withValueSupplier({ asStr })
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other) && other is Timestamp && utcEpochMicros == other.utcEpochMicros
    }

    override fun hashCode(): Int {
        val result = super.hashCode()
        return 31 * result + (utcEpochMicros xor (utcEpochMicros ushr 32)).toInt()
    }
}