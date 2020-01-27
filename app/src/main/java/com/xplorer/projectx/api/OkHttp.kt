package com.xplorer.projectx.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class OkHttp {
    val client: OkHttpClient
        get() {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        }
}