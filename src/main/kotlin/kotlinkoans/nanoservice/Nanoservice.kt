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
import spark.Request
import spark.Response
import spark.Spark.port
import spark.Spark.get
import spark.Spark.notFound
import spark.Spark.threadPool
import spark.Route

const val ROOT_PATH = "/"

private val LOGGER = LoggerFactory.getLogger("kotlinkoans.nanoservice.NanoserviceKt")
private val GSON = Gson()
private val NO_COMMAND_PARAMETERS = arrayOf<String>()

fun startNanoservice(port: Int, commands: Map<String, String>) {
    port(port)
    threadPool(Runtime.getRuntime().availableProcessors())
    LOGGER.info("setPort($port)")
    get(ROOT_PATH, "application/json") { _, res ->
        res.setResponseHeaders()
        ResponseMsg.success(commands)
    }
    notFound { req, res ->
        res.setResponseHeaders()
        ResponseMsg.failure("Command not supported: '${req.pathInfo()}', use: $commands")
    }
    LOGGER.info("Default page: $commands")
    commands.keys.forEach {
        LOGGER.info("wiring command: '$it'")
        val action = Route { req, res ->
            val params: Array<String> = req.extractCommandParameters(it)
            res.setResponseHeaders()
            ResponseMsg.success("Hello! ${params.toList()}")
        }
        get(routePath(it, false), "application/json", action) // parameter-less
        get(routePath(it, true), "application/json", action) // with parameters
    }
}

/**
 * Represents the response that travels back to the requester upon having sent
 * an http request. The response is JSON serialised before it is sent back
 */
data class ResponseMsg<T>(val status: Status, val cargo: T) {
    companion object {
        fun <T> success(obj: T): String = GSON.toJson(ResponseMsg(Status.Success, obj))

        fun <T> failure(obj: T): String = GSON.toJson(ResponseMsg(Status.Failure, obj))

        enum class Status {
            Success, Failure
        }
    }
}

fun requestHeader(resource: String) = with(StringBuilder()) {
    append("GET $resource HTTP/1.1\r\n")
    append("Host: ignore\r\n")
    append("Accept: application/json\r\n")
    append("\r\n")
    toString()
}

private fun routePath(command: String, withParameters: Boolean) = with(StringBuilder(ROOT_PATH)) {
    append(command)
    if (withParameters) {
        append("/*")
    }
    toString()
}.also { LOGGER.info("routePath($command) = $it") }

private fun Response.setResponseHeaders() {
    header("Content-Type", "application/json")
    header("Transfer-Encoding", "chunked")
}

/**
 * The request will contain a path info that looks like:
 *     /command/param1/param2/param//with//white//space//number3/param4
 */
private fun Request.extractCommandParameters(command: String): Array<String> {
    val pathInfo = pathInfo()
    LOGGER.info("req.pathInfo(): $pathInfo")
    val len = routePath(command, false).length
    val params = if (pathInfo.length > len) pathInfo.substring(len + 1) else null
    if (params?.isNotBlank() == true) {
        return params.replace("//".toRegex(), " ").split("/".toRegex()).toTypedArray()
    }
    return NO_COMMAND_PARAMETERS
}

fun main(args: Array<String>) {
    val port = if (args.isNotEmpty()) args[0].toInt() else 2020
    startNanoservice(
        port, mapOf(
            "hello" to "[/hello | /hello/param1/../paramn ('//' is replaced by white)]: returns a greeting"
        )
    )
}
