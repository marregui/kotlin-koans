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
    // kotlin
    const val kotlin = "1.4.10"
    const val kotlinCoroutines = "1.3.9"

    // nano service
    const val sparkJava = "2.9.3"
    const val gson = "2.8.6"

    // logging
    const val slf4jApi = "1.7.30"

    // testing
    const val junit_jupiter = "5.6.2"
    const val junit_jupiter_api = "5.6.2"
    const val junit_jupiter_engine = "5.6.2"
    const val hamcrest = "2.2"
    const val mockito_core = "3.5.10"
}

object Libs {
    const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    const val sparkJava = "com.sparkjava:spark-core:${Versions.sparkJava}"
    const val gson = "com.google.code.gson:gson:${Versions.gson}"
    const val slf4jApi = "org.slf4j:slf4j-api:${Versions.slf4jApi}"
    const val slf4jLog4jBinding = "org.slf4j:slf4j-log4j12:${Versions.slf4jApi}"
    const val junitJupiter = "org.junit.jupiter:junit-jupiter:${Versions.junit_jupiter}"
    const val junitJupiterApi = "org.junit.jupiter:junit-jupiter-api:${Versions.junit_jupiter_api}"
    const val junitJupiterEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit_jupiter_engine}"
    const val hamcrest = "org.hamcrest:hamcrest:${Versions.hamcrest}"
    const val mokitoCore = "org.mockito:mockito-core:${Versions.mockito_core}"
}
