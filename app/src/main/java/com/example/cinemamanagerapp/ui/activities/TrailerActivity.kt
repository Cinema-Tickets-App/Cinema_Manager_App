package com.example.cinemamanagerapp.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cinemamanagerapp.R



class TrailerActivity : AppCompatActivity() {
    private  var webView: WebView? = null
    private var url : String? = null
     var context = this

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trailer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        url = intent.getStringExtra("url")
        webView = findViewById(R.id.wvTrailer)
        webView?.loadUrl(url!!)
        webView!!.settings.javaScriptEnabled = true
        webView!!.webViewClient = WebViewClient()
    }

}