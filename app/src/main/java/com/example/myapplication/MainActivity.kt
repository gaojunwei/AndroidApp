package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.click.MyLongClickListener

/**
 * 参考网址
 * https://juejin.cn/post/7021682733718077454
 *
 * https://www.geeksforgeeks.org/dynamic-autocompletetextview-in-kotlin/
 * https://www.delftstack.com/zh/howto/kotlin/initialize-array-in-kotlin/#%25E5%259C%25A8-kotlin-%25E4%25B8%25AD%25E4%25BD%25BF%25E7%2594%25A8-arrayof-%25E5%2587%25BD%25E6%2595%25B0%25E5%25A3%25B0%25E6%2598%258E%25E5%2592%258C%25E5%2588%259D%25E5%25A7%258B%25E5%258C%2596%25E6%2595%25B0%25E7%25BB%2584
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val loginName: TextView by lazy { findViewById<TextView>(R.id.login_name) }
    private val loginPwd: TextView by lazy { findViewById<TextView>(R.id.login_pwd) }

    private val logInBtn: Button by lazy { findViewById<Button>(R.id.login_btn) }
    private val serverSettingBtn: Button by lazy { findViewById<Button>(R.id.server_setting_btn) }
    private val listBtn: Button by lazy { findViewById<Button>(R.id.list_btn) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logInBtn.setOnClickListener(this)
        listBtn.setOnClickListener(this)
        serverSettingBtn.setOnLongClickListener(MyLongClickListener())
        //设置背景图片
        this.window.setBackgroundDrawableResource(R.drawable.login)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.login_btn -> {
                var userName = loginName.text.toString()
                var userPwd = loginPwd.text.toString()

                Toast.makeText(this, "登录成功，账号:$userName!", Toast.LENGTH_SHORT).show()
                //跳转到账户中心，并传参
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                intent.putExtra("login_name", loginName.text.toString())
                intent.putExtra("login_pwd", loginPwd.text.toString())
                startActivity(intent)
            }
            R.id.list_btn -> {
                //跳转到数据页面
                val intent = Intent(this@MainActivity, ListActivity::class.java)
                startActivity(intent)
            }
        }
    }
}