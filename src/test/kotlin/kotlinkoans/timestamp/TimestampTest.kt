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

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.*
import java.time.temporal.TemporalAccessor

class TimestampTest {
    @BeforeEach
    fun beforeEach() {
        // some tests require fixing the instant, the rest don't
        TimestampParser.withFixedInstant(null)
    }

    @Test
    fun test_format_followed_by_parse_results_in_original_epoch_matching_precision_millis() {
        val microPrecisionInstant: Instant = TimestampParser.now()
        val startMillis = microPrecisionInstant.toEpochMilli()
        val formattedMillis: String = TimestampParser.formatMillis(startMillis)
        val parsedMicros: Long = TimestampParser.parse(formattedMillis)
        assertThat(startMillis * 1000L, `is`(parsedMicros))
    }

    @Test
    fun test_format_followed_by_parse_results_in_original_epoch_matching_precision_micros() {
        val microPrecisionInstant: Instant = TimestampParser.now()
        val startMicros: Long = TimestampParser.toEpochMicro(microPrecisionInstant)
        val formattedMicros: String = TimestampParser.format(startMicros)
        val parsedMicros: Long = TimestampParser.parse(formattedMicros)
        assertThat(startMicros, `is`(parsedMicros))
    }

    @Test
    fun test_format() {
        assertThat(TimestampParser.format(257472000123456L), `is`("1978-02-28T00:00:00.123456"))
        assertThat(TimestampParser.format(257558399999997L), `is`("1978-02-28T23:59:59.999997"))
    }

    @Test
    fun test_format_millis() {
        assertThat(TimestampParser.formatMillis(257472000000L), `is`("1978-02-28T00:00:00"))
        assertThat(TimestampParser.formatMillis(257558399999L), `is`("1978-02-28T23:59:59.999"))
    }

    @Test
    fun test_parse_date_time_with_time_zone() {
        assertThat(TimestampParser.parse("2020-09-09 00:00:00Z"), `is`(1599609600000000L))
        assertThat(TimestampParser.format(1599609600000000L), `is`("2020-09-09T00:00:00"))
        assertThat(TimestampParser.parse("2020-09-09 01:00:00+00"), `is`(1599613200000000L))
        assertThat(TimestampParser.format(1599613200000000L), `is`("2020-09-09T01:00:00"))
        assertThat(TimestampParser.parse("2020-09-09 04:00:00-03:00"), `is`(1599634800000000L))
        assertThat(TimestampParser.format(1599634800000000L), `is`("2020-09-09T07:00:00"))
        assertThat(TimestampParser.parse("2020-09-09 04:00:00+0300"), `is`(1599613200000000L))
        assertThat(TimestampParser.format(1599613200000000L), `is`("2020-09-09T01:00:00"))
        assertThat(TimestampParser.parse("2020-09-09 04:00:00.123456+03:00"), `is`(1599613200123456L))
        assertThat(TimestampParser.format(1599613200123456L), `is`("2020-09-09T01:00:00.123456"))
        assertThat(TimestampParser.parse("2020-09-09 04:00:00.123456+0000"), `is`(1599624000123456L))
        assertThat(TimestampParser.parse("2020-09-09 04:00:00.123456789-0000"), `is`(1599624000123456L))
        assertThat(TimestampParser.format(1599624000123456L), `is`("2020-09-09T04:00:00.123456"))
    }

    @Test
    fun test_to_string() {
        val ts = Timestamp("1978-02-28 00:00:00.123456")
        assertThat(ts.utcEpochMicros, `is`(257472000123456L))
        assertThat(ts.asStr, `is`("1978-02-28T00:00:00.123456"))
        assertThat(ts.toString(), `is`("Timestamp{utcEpochMicros=257472000123456, asStr=1978-02-28T00:00:00.123456}"))
    }
}
