package com.example.myapplication.util

import android.content.Context
import android.widget.Toast

object ToastUtils {
    private var toast: Toast? = null

    /**
     * 短时间显示Toast
     * @param context
     * @param message 显示字符串
     * @param gravity 显示位置
     * @param duration 持续时间
     */
    fun showToast(context: Context?, message: CharSequence): Toast? {
        if (toast == null) {
            /*toast为空新建*/
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            return toast
        } else {
            toast?.cancel()
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            return toast
        }
    }
}