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
package com.xplorer.projectx.networking

import android.os.Handler
import android.os.Looper

import java.util.concurrent.Executor
import java.util.concurrent.Executors

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppExecutors(private val diskIO: Executor, private val networkIO: Executor) {
    private val mainIO: Executor

    @Inject
    constructor() : this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(5))
    init {
        this.mainIO = MainThreadExecutor()
    }

    internal class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun diskIO(): Executor {
        return diskIO
    }

    fun mainIO(): Executor {
        return mainIO
    }
}
