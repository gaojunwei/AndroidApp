package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.util.AppUtils
import com.example.myapplication.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap


class ListActivity : AppCompatActivity() {

/*    var array = arrayOf(
        "Melbourne", "Vienna", "Vancouver", "Toronto", "Calgary",
        "Adelaide", "Perth", "Auckland", "Helsinki", "Hamburg", "Munich",
        "New York", "Sydney", "Paris", "Cape Town", "Barcelona", "London", "Bangkok"
    )*/

    val gson = Gson()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        findViewById<Button>(R.id.change).setOnClickListener {
            ListRoomListTask().start()
        }
    }

    private val COUNTRIES = arrayOf(
        "Belgium", "France", "Italy", "Germany", "Spain"
    )

    private fun listRoomList(data: String?) {
        Log.i("教室列表数据AA", "$data")
        /**
         * 新建组件
         */
        var autoTextView =  findViewById<AutoCompleteTextView>(R.id.roomName)
        /*f(autoTextView == null){
            Log.i("检测元素是否已经创建","未创建")
            autoTextView = AutoCompleteTextView(this)
        }*/
        val button = Button(this)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )


        autoTextView.id =R.id.roomName
        autoTextView.layoutParams = layoutParams
        button.layoutParams = layoutParams
        button.layout
        layoutParams.setMargins(30, 30, 30, 30)
        autoTextView.hint ="请选择"
        button.text = "Submit"

        /**
         * 添加组件到布局页面
         */
        val linearLayout = findViewById<LinearLayout>(R.id.list_layout)
        linearLayout?.addView(autoTextView)
        linearLayout?.addView(button)

        val dataMap = gson.fromJson<HashMap<String, Any>>(data, HashMap::class.java)
        var data = dataMap["data"] as LinkedTreeMap<String, Any>
        var list = data["list"] as List<LinkedTreeMap<String, Any>>
        var dataList = (list.map { item->item["roomName"] })

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, dataList
        )
        autoTextView.setAdapter(adapter)

    }

    private fun toastMsg(msg: String?) {
        val randoms = (0..10).random()
        ToastUtils.showToast(applicationContext, "处理结果:$msg $randoms")?.show()
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
            Log.i("响应code", "测试 $code $msg")
            this@ListActivity.runOnUiThread {
                toastMsg(msg)
            }
            this@ListActivity.runOnUiThread {
                listRoomList(result)
            }
        }

    }
}