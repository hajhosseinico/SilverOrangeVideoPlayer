package com.silverorange.videoplayer.view

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.silverorange.videoplayer.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        setStatusBarColor()
    }

    private fun setStatusBarColor() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            findViewById<Toolbar>(R.id.toolbar).visibility = View.GONE
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            findViewById<Toolbar>(R.id.toolbar).visibility = View.VISIBLE
        }
    }
}