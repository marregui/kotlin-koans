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

object Versions {
    // kotlin:
    val kotlin = "1.4.10"
    val kotlinCoroutines = "1.3.9"

    // logging
    val log4j_slf4j = "2.13.0"

    // testing
    val junit_jupiter = "5.6.2"
    val junit_jupiter_api = "5.6.2"
    val junit_jupiter_engine = "5.6.2"
    val hamcrest = "2.2"
    val mockito_core = "3.5.10"
}

object Libs {
    val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
    val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"

    val logging = "org.apache.logging.log4j:log4j-slf4j-impl:${Versions.log4j_slf4j}"
    val junitJupiter = "org.junit.jupiter:junit-jupiter:${Versions.junit_jupiter}"
    val junitJupiterApi = "org.junit.jupiter:junit-jupiter-api:${Versions.junit_jupiter_api}"
    val junitJupiterEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit_jupiter_engine}"
    val hamcrest = "org.hamcrest:hamcrest:${Versions.hamcrest}"
    val mokitoCore = "org.mockito:mockito-core:${Versions.mockito_core}"
}
