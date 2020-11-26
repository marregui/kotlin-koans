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

package kotlinkoans.nanoservice

import com.google.gson.Gson
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.nio.charset.Charset
import kotlin.system.exitProcess

private val UTF8 = Charset.forName("UTF-8")
private val GSON = Gson()
private val LOGGER = LoggerFactory.getLogger("kotlinkoans.nanoservice.NanoclientKt")
private val COLON_SPACE = ": ".toRegex()
private val SPACE = " ".toRegex()

private fun prepareCommand(params: Array<String>, offset: Int = 2) = with(StringBuilder(ROOT_PATH)) {
    for (i in offset until params.size) {
        append(params[i].replace(SPACE, "//")).append("/")
    }
    if (isNotEmpty()) {
        setLength(length - 1)
    }
    toString()
}

private fun slurp(br: BufferedReader) = GSON.fromJson(with(StringBuilder()) {
    var httpStatus: Int
    var nextChunkSize = -1L
    var slurpStatus = SlurpStatus.ExpectingHTTPBegin
    var line: String
    loop@ while (null != br.readLine().also { line = it }) {
        when (slurpStatus) {
            SlurpStatus.ExpectingHTTPBegin -> if (line.toUpperCase().startsWith("HTTP")) {
                httpStatus = line.split(SPACE)[1].toInt()
                slurpStatus = SlurpStatus.ReadingHeaders
                LOGGER.info("Status: $httpStatus")
            } else {
                throw IllegalStateException("[$slurpStatus] unexpected: $line")
            }
            SlurpStatus.ReadingHeaders -> if (line.isNotBlank()) {
                val header = line.split(COLON_SPACE)
                check(header.size == 2) { "[$slurpStatus] unexpected:$line" }
                LOGGER.info("Header [${header[0].trim { it <= ' ' }}] -> ${header[1].trim { it <= ' ' }}")
            } else {
                slurpStatus = SlurpStatus.ReadingSize
                setLength(0)
                LOGGER.info("End of HTTP headers")
            }
            SlurpStatus.ReadingSize -> try {
                nextChunkSize = hexToDec(line)
                LOGGER.info("Size: $nextChunkSize ($line)")
                if (nextChunkSize == 0L) {
                    slurpStatus = SlurpStatus.Completed
                    LOGGER.info("EOF")
                } else {
                    slurpStatus = SlurpStatus.ReadingText
                }
            } catch (e: Exception) {
                throw IllegalStateException("[$slurpStatus] unexpected: $line")
            }
            SlurpStatus.ReadingText -> {
                check(!(nextChunkSize <= 0 || line.length.toLong() != nextChunkSize)) {
                    "[$slurpStatus] size should be $nextChunkSize but is ${line.length}: $line"
                }
                append(line)
                slurpStatus = SlurpStatus.ReadingSize
                LOGGER.info(line)
            }
            SlurpStatus.Completed -> break@loop
        }
    }
    toString()
}, ResponseMsg::class.java)

private enum class SlurpStatus {
    ExpectingHTTPBegin, ReadingHeaders, ReadingSize, ReadingText, Completed
}

private fun hexToDec(hex: String): Long {
    var dec = 0L
    var pow = 1L
    for (i in (hex.length - 1) downTo 0) {
        dec += hexToDec(hex[i]) * pow
        pow *= 16L
    }
    return dec
}

private fun hexToDec(c: Char): Int = when (val lc = Character.toLowerCase(c)) {
    in 'a'..'z' -> lc - 'a' + 10
    in '0'..'9' -> lc - '0'
    else -> throw IllegalArgumentException("Not a valid hex: $c")
}

fun main(args: Array<String>) {
    if (args.size < 3) {
        println("Syntax: java -cp.. kotlinkoans.nanoservice.Nanoclient host port command [parameters]")
        println("       note: the param 'this//is//cool' becomes 'this is cool' ('//' is interpreted as blank)")
        exitProcess(-1)
    }
    val host = args[0]
    val port = args[1].toInt()
    val command = prepareCommand(args)
    Socket(host, port).use { sck ->
        BufferedReader(InputStreamReader(sck.getInputStream(), UTF8)).use { sckIn ->
            BufferedWriter(OutputStreamWriter(sck.getOutputStream(), UTF8)).use { sckOut ->
                sckOut.write(requestHeader(command))
                sckOut.flush()
                println(slurp(sckIn))
            }
        }
    }
}
