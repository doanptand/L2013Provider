package com.ddona.provider

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ddona.provider.util.Const

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val values = ContentValues()
        values.put(Const.COL_USERNAME, "doandeptrai")
        values.put(Const.COL_PASSWORD, "doandeptrai")
        values.put(Const.COL_ROLE, "Admin")
        contentResolver.insert(
            Uri.parse("content://com.ddona.provider.provider.UserProvider/user"),
            values
        )
//        contentResolver.query(
//            Uri.parse("content://com.ddona.provider.provider.UserProvider/user"),
//            null,
//            null,
//            null,
//            null
//        )
    }
}