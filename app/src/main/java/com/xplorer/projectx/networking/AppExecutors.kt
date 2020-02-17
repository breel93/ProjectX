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