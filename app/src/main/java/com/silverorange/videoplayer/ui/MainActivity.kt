package com.silverorange.videoplayer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.silverorange.videoplayer.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setStatusBarColor()
    }

    private fun setStatusBarColor(){
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
    }
}