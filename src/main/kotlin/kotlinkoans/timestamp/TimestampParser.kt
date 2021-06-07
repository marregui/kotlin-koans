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

import java.time.*
import java.time.format.DateTimeFormatterBuilder
import java.time.format.ResolverStyle
import java.time.format.SignStyle
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalQuery
import java.util.*
import java.util.concurrent.atomic.AtomicReference

class TimestampParser private constructor() {
    companion object {
        private val CLOCK: Clock = Clock.systemUTC()
        private val FIXED_INSTANT = AtomicReference<Instant>()

        fun withFixedInstant(i: Instant?): Instant? {
            FIXED_INSTANT.set(i)
            return i
        }

        fun now(): Instant = FIXED_INSTANT.get() ?: CLOCK.instant()

        fun toEpochMicro(i: Instant): Long = (i.epochSecond * 1000_000_000L + i.nano) / 1000L

        fun parse(dt: String?): Long {
            if (dt == null || dt.isBlank()) {
                return toEpochMicro(now())
            }
            val t = TIMESTAMP_SPEC.parseBest(
                dt,
                TemporalQuery<Any> { temporal: TemporalAccessor? -> ZonedDateTime.from(temporal) },
                TemporalQuery<Any> { temporal: TemporalAccessor? -> LocalDateTime.from(temporal) },
                TemporalQuery<Any> { temporal: TemporalAccessor? -> LocalDate.from(temporal) },
                TemporalQuery<Any> { temporal: TemporalAccessor? -> LocalTime.from(temporal) })
            return toEpochMicro(
                when (t) {
                    is ZonedDateTime -> t.withZoneSameInstant(ZoneOffset.UTC).toInstant()
                    is LocalDateTime -> t.toInstant(ZoneOffset.UTC)
                    else -> throw IllegalStateException()
                }
            )
        }

        fun format(timestampMicros: Long): String {
            val prefix = timestampMicros / 1000_000L
            val suffix = timestampMicros - prefix * 1000_000L
            val epochMillis = (timestampMicros - suffix) / 1000L
            return TIMESTAMP_SPEC.format(
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(epochMillis)
                        .plusNanos(suffix * 1000L), ZoneOffset.UTC
                )
            )
        }

        fun formatMillis(millis: Long): String {
            return TIMESTAMP_SPEC.format(
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(millis), ZoneOffset.UTC
                )
            )
        }

        val TIMESTAMP_SPEC = DateTimeFormatterBuilder()
            .parseCaseInsensitive() // Date part
            .optionalStart()
            .appendValue(ChronoField.YEAR, 4, 10, SignStyle.NOT_NEGATIVE)
            .appendLiteral('-')
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendLiteral('-')
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .optionalEnd() // Time part
            .optionalStart()
            .padNext(1)
            .optionalStart().appendLiteral('T').optionalEnd()
            .optionalStart()
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .optionalStart()
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
            .optionalEnd()
            .optionalStart().appendPattern("[Z][VV][x][xx][xxx]").optionalEnd()
            .optionalEnd()
            .optionalEnd()
            .toFormatter(Locale.ENGLISH)
            .withResolverStyle(ResolverStyle.STRICT)
    }
}