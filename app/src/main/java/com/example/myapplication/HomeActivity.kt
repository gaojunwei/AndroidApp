package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * 登录后
 */
class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val loginName = this.intent.getStringExtra("login_name")
        val loginPwd = this.intent.getStringExtra("login_pwd")

        Log.i("获取参数值",loginName.toString()+" 密码:"+loginPwd.toString())

    }
}