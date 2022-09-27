package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.*
import android.widget.MultiAutoCompleteTextView.CommaTokenizer
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.util.AppUtils
import com.example.myapplication.util.ToastUtils
import com.google.gson.Gson


class ListActivity : AppCompatActivity() {

/*    var array = arrayOf(
        "Melbourne", "Vienna", "Vancouver", "Toronto", "Calgary",
        "Adelaide", "Perth", "Auckland", "Helsinki", "Hamburg", "Munich",
        "New York", "Sydney", "Paris", "Cape Town", "Barcelona", "London", "Bangkok"
    )*/

    val gson = Gson()
    private val multiAutoCompleteTextView: MultiAutoCompleteTextView by lazy { findViewById<MultiAutoCompleteTextView>(R.id.mText) }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        findViewById<Button>(R.id.change).setOnClickListener {
            ListRoomListTask().start()
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line, COUNTRIES
        )
        val textView = findViewById<MultiAutoCompleteTextView>(R.id.mText)
        textView.setAdapter(adapter)
        textView.setTokenizer(CommaTokenizer())
    }

    private val COUNTRIES = arrayOf(
        "Belgium", "France", "Italy", "Germany", "Spain"
    )

    private fun listRoomList(data: String?) {
        Log.i("教室列表数据AA","$data")

        //multiAutoCompleteTextView.
    }

    private fun toastMsg(msg: String?) {
        val randoms = (0..10).random()
        ToastUtils.showToast(applicationContext,"处理结果:$msg $randoms")?.show()
    }

    /**
     * 获取教室列表
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
            Log.i("响应code","测试 $code $msg")
            this@ListActivity.runOnUiThread {
                toastMsg(msg)
            }
            this@ListActivity.runOnUiThread {
                listRoomList(result)
            }
        }

    }
}