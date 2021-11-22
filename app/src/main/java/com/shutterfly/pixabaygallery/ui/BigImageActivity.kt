package com.shutterfly.pixabaygallery.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.shutterfly.pixabaygallery.databinding.ActivityBigImageBinding

class BigImageActivity: AppCompatActivity() {

    private lateinit var bigImageBinding: ActivityBigImageBinding

    companion object {
        private const val PREVIEW_URL = "preview_url"

        fun getIntent(context: Context, mUrl: String): Intent {
            val intent = Intent(context, BigImageActivity::class.java)
            intent.putExtra(PREVIEW_URL, mUrl)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bigImageBinding = ActivityBigImageBinding.inflate(layoutInflater)
        setContentView(bigImageBinding.root)

        setImage()
        setToolBar()
        setBackButton()
    }

    private fun setToolBar() {
        setSupportActionBar(bigImageBinding.bigImageToolbar)
    }

    private fun setBackButton(){
        bigImageBinding.bigImageBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setImage(){
        val url = intent.getStringExtra(PREVIEW_URL)
        Glide.with(this)
            .load(url)
            .into(bigImageBinding.bigImageImage)
    }
}