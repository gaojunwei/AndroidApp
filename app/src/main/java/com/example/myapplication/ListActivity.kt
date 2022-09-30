package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.util.AppUtils
import com.example.myapplication.util.ToastUtils
import com.google.gson.Gson


class ListActivity : AppCompatActivity() {

    val gson = Gson()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        findViewById<Button>(R.id.change).setOnClickListener {
            ListRoomListTask().start()
        }
        /**
         * 手机震动
         */
        findViewById<Button>(R.id.refresh).setOnClickListener {
            vibratePhone()
        }
        /**
         * 获取通讯录
         */
        findViewById<Button>(R.id.getContacts).setOnClickListener {
            //确定您是否已被授予特定权限
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    1
                )
            } else {
                readContacts()
            }
        }

        /**
         * 获取设备信息
         */
        findViewById<Button>(R.id.deviceInfo).setOnClickListener {
            var brand = Build.BRAND //获取手机厂商
            var model = Build.MODEL //获取设备型号
            var systemVersion = Build.VERSION.RELEASE //获取当前系统版本

            //获取屏幕分辨率
            val dm: DisplayMetrics = resources.displayMetrics
            val screenWidth = dm.widthPixels
            val screenHeight = dm.heightPixels

            //获取拨号卡的运营商
            var simOperatorName = getSimOperatorName()

            var deviceInfo = "手机厂商:$brand;\n设备型号:$model;\n系统版本:$systemVersion;\n分辨率:(宽:$screenWidth,高:$screenHeight);\nSIM运营商:$simOperatorName"
            findViewById<TextView>(R.id.resultData).text = deviceInfo
        }

        /**
         * 示例 - 在默认浏览器中打开 URL 的 Android 应用程序
         */
        findViewById<Button>(R.id.webView).setOnClickListener {
            //第 1 步：为 ACTION_VIEW 创建一个 Intent
            val openURL = Intent(Intent.ACTION_VIEW)
            //第 2 步：将 Uri 设置为意图的数据
            openURL.data = Uri.parse("https://www.baidu.com/")
            //第 3 步：根据意图启动 Activity
            startActivity(openURL)
        }
    }

    /**
     * 手机震动
     */
    private fun vibratePhone() {
        val vibrator = this?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //低版本处理
            vibrator.vibrate(200)
        }
        val randoms = (0..10).random()
        ToastUtils.showToast(applicationContext, "手机震动调用成功 $randoms")?.show()
    }

    /**
     * 教室下拉框数据填装
     */
    private fun listRoomList(data: String?) {
        findViewById<TextView>(R.id.resultData).text = data
    }

    /**
     * 读取通讯录
     */
    @SuppressLint("Range")
    private fun readContacts() {
        Log.i("授权", "允许读取通讯录")
        var cursor: Cursor? = null
        try {
            cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            )
            if (cursor != null) {
                var connects = ArrayList<Map<String, String>>()
                while (cursor.moveToNext()) {
                    var uerName =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    var number =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    var userMap = mapOf<String, String>("uerName" to uerName, "number" to number)
                    connects.add(userMap)
                }
                Log.i("通讯录数据", gson.toJson(connects))
                findViewById<TextView>(R.id.resultData).text = gson.toJson(connects)
            }
        } catch (e: Exception) {
            toastMsg("读取通讯录异常失败")
        } finally {
            cursor?.close()
        }
    }

    /**
     *
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts()
                } else {
                    toastMsg("用户拒绝授权")
                }
            }
            else -> {

            }
        }
    }

    /**
     * 请求http接口获取教室列表
     */
    private inner class ListRoomListTask : Thread() {
        override fun run() {
            var url = "http://1.1.1.254:8090/mitg-commander/park/room/page?page=1&limit=10"
            var httpClient = AppUtils
            var result = httpClient.get(url)
            Log.i("获取教室列表", "$result")
            //判断请求是否成功
            val dataMap = gson.fromJson<HashMap<String, Any>>(result, HashMap::class.java)
            var code = (dataMap["code"] as Double).toInt()
            var msg = dataMap["msg"] as String
            Log.i("响应code", "测试 $code $msg")
            this@ListActivity.runOnUiThread {
                toastMsg(msg)
            }
            this@ListActivity.runOnUiThread {
                listRoomList(result)
            }
        }
    }

    private fun toastMsg(msg: String?) {
        val randoms = (0..10).random()
        ToastUtils.showToast(applicationContext, "处理结果:$msg $randoms")?.show()
    }

    /**
     * 获取运营商名称
     */
    private fun getSimOperatorName(): String {
        var TAG = "获取运营商信息"
        val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simOperator = tm.simOperator

        if (tm.simState != TelephonyManager.SIM_STATE_READY) {
            when (tm.simState) {
                TelephonyManager.SIM_STATE_ABSENT -> {//1
                    Log.i(TAG, "没有Sim卡")
                }
                TelephonyManager.SIM_STATE_PIN_REQUIRED -> {//2
                    Log.i(TAG, "Sim卡状态锁定，需要PIN解锁")
                }
                TelephonyManager.SIM_STATE_PUK_REQUIRED -> {//3
                    Log.i(TAG, "Sim卡状态锁定，需要PUK解锁")
                }
                TelephonyManager.SIM_STATE_NETWORK_LOCKED -> {//4
                    Log.i(TAG, "需要网络PIN码解锁")
                }
            }
            return "获取失败"
        }
        Log.i(TAG, "getSimOperator()获取的MCC+MNC为：$simOperator")
        Log.i(TAG, "getSimOperatorName()方法获取的运营商名称为:${tm.simOperatorName} ")
        return if ("46001" == simOperator || "46006" == simOperator || "46009" == simOperator) {
            "中国联通"
        } else if ("46000" == simOperator || "46002" == simOperator || "46004" == simOperator || "46007" == simOperator) {
            "中国移动"
        } else if ("46003" == simOperator || "46005" == simOperator || "46011" == simOperator) {
            "中国电信"
        } else if ("46020" == simOperator) {
            "中国铁通"
        } else {
            "未知"
        }
    }
}