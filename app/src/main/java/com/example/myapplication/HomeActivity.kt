package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * 登录后
 */
class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private val newsBtn: Button by lazy { findViewById<Button>(R.id.news_btn) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val loginName = this.intent.getStringExtra("login_name")
        val loginPwd = this.intent.getStringExtra("login_pwd")

        Log.i("获取参数值", loginName.toString() + " 密码:" + loginPwd.toString())

        newsBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.news_btn -> {
                Thread(){
                    //获取新闻列表
                    var url = "https://demo-api.apipost.cn/api/demo/news_list?mobile=18289454846&theme_news=国际新闻&page=1&pageSize=20"
                    var httpClient = HttpUtil()
                    var result = httpClient.get(url)
                    Log.i("响应数据", "$result")
                    //Toast.makeText(HomeActivity,"登录成功，账号:$result!",Toast.LENGTH_SHORT).show()
                }.start()
            }
        }
    }
}