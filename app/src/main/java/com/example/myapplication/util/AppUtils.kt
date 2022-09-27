package com.example.myapplication.util

import okhttp3.OkHttpClient
import okhttp3.Request
import java.nio.charset.Charset

object AppUtils {
    /**
     * 发送get请求
     */
    fun get(url: String): String? {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        return response.body()?.bytes()?.let { String(it, Charset.forName("utf-8")) }
    }
}