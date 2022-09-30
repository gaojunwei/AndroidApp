package com.example.myapplication.util

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.util.Log
import androidx.annotation.RequiresApi
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

    /**
     * 获取运营商的名字
     */
    fun getOperatorName(context: Context): String? {
        /*
         * getSimOperatorName()就可以直接获取到运营商的名字
         * 也可以使用IMSI获取，getSimOperator()，然后根据返回值判断，例如"46000"为移动
         * IMSI相关链接：http://baike.baidu.com/item/imsi
         */
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        // getSimOperatorName就可以直接获取到运营商的名字
        return telephonyManager.simOperatorName
    }



}