package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * https://juejin.cn/post/7021682733718077454
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val loginName: TextView by lazy { findViewById<TextView>(R.id.login_name) }
    private val loginPwd: TextView by lazy { findViewById<TextView>(R.id.login_pwd) }

    /* private val myImg: ImageView by lazy { findViewById<ImageView>(R.id.myImg) }
     private val imageView: ImageView by lazy { findViewById<ImageView>(R.id.imageView) }*/
    private val logInBtn: Button by lazy { findViewById<Button>(R.id.login_btn) }
    private val serverSettingBtn: Button by lazy { findViewById<Button>(R.id.server_setting_btn) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logInBtn.setOnClickListener(this)
        serverSettingBtn.setOnClickListener(this)
        //设置背景图片
        this.window.setBackgroundDrawableResource(R.drawable.login)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.login_btn -> {
                var userName = loginName.text.toString()
                var userPwd = loginPwd.text.toString()
                if(userName.isNullOrBlank() || userName != "admin"){
                    Toast.makeText(this,"账号错误", Toast.LENGTH_SHORT).show()
                    return
                }
                if(userPwd.isNullOrBlank() || userPwd != "123456"){
                    Toast.makeText(this,"密码错误", Toast.LENGTH_SHORT).show()
                    return
                }

                Toast.makeText(this,"登录成功，账号:" + loginName.text.toString()+"!",Toast.LENGTH_SHORT).show()
                //跳转到账户中心，并传参
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                intent.putExtra("login_name", loginName.text.toString())
                intent.putExtra("login_pwd", loginPwd.text.toString())
                startActivity(intent)
            }
        }
    }
}