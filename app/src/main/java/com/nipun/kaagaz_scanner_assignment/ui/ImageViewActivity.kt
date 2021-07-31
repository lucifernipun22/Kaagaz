package com.nipun.kaagaz_scanner_assignment.ui

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nipun.kaagaz_scanner_assignment.R
import kotlinx.android.synthetic.main.activity_image.*


class ImageViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        val image =intent.getStringExtra("name")
        Glide.with(this).load(image).into(iv_Image)
    }
}