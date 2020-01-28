package com.xplorer.projectx.networking

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

/**
 *  Designed and developed by ProjectX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@Singleton
class CoroutineContextProviderImpl @Inject constructor(): CoroutineContextProvider {
    override val main: CoroutineContext by lazy { Dispatchers.Main }
    override val io: CoroutineContext by lazy { Dispatchers.IO }
}

class TestCoroutineContextProviderImpl: CoroutineContextProvider {
    override val main: CoroutineContext by lazy { Dispatchers.Unconfined }
    override val io: CoroutineContext by lazy { Dispatchers.Unconfined }
}

interface CoroutineContextProvider {
    val main: CoroutineContext
    val io: CoroutineContext
}

