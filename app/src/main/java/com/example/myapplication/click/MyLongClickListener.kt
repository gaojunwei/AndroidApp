package com.example.myapplication.click

import android.view.View
import android.widget.Toast

class MyLongClickListener:View.OnLongClickListener {

    override fun onLongClick(v: View?): Boolean {
        longClickToast(v)
        return true
    }

    private fun longClickToast(v: View?){
        Toast.makeText(v?.context, "长按触发事件", Toast.LENGTH_SHORT).show()
    }
}