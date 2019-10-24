package io.haruharu.webview


import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import kotlinx.android.synthetic.main.layout_web_view.*


class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var mUrl: String? = null

    @SuppressLint("InlinedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_web_view)

        this.mUrl = intent.getStringExtra(KEY_URL)
        if (!this.mUrl!!.startsWith("http"))
            this.mUrl = "http://" + this.mUrl!!

        if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT)
            window.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)

        initView()
        loadUrl(mUrl ?: "")
    }

    override fun onBackPressed() {
        if (goBack()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        frameLayout_webViewContainer.removeView(webView)
        webView.removeAllViews()
        webView.destroy()
        super.onDestroy()
    }

    /**
     */

    private fun initView() {
        val title = intent.getStringExtra(KEY_TITLE) ?: ""
        setTitle(title)

        linearLayout_toolbar.updateLayoutParams<RelativeLayout.LayoutParams> { topMargin = statusBarSize }

        webView = WebView(this)
        webView.layoutParams = ViewGroup.LayoutParams(-1, -1)
        webView.webViewClient = getWebViewClient()
        webView.webChromeClient = getWebChromeClient()
        webView.setInitialScale(1)
        initWebSettings(webView.settings)

        frameLayout_webViewContainer.layoutParams = RelativeLayout.LayoutParams(-1, -1)
        frameLayout_webViewContainer.addView(webView)

        imageView_back.setOnClickListener { onClose() }
        imageView_refresh.setOnClickListener { onRefresh() }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("SetJavaScriptEnabled", "NewApi")
    private fun initWebSettings(webSettings: WebSettings) {
        webSettings.pluginState = WebSettings.PluginState.ON
        webSettings.useWideViewPort = true
        webSettings.defaultZoom = WebSettings.ZoomDensity.MEDIUM
        webSettings.builtInZoomControls = false
        webSettings.setSupportZoom(false)
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.allowFileAccess = true
        webSettings.domStorageEnabled = true
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true
        webSettings.setAppCacheEnabled(true)

        if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT)
            webSettings.displayZoomControls = false
    }

    fun getWebViewClient(): WebViewClient {
        return MocaWebViewClient()
    }

    fun getWebChromeClient(): WebChromeClient {
        return MocaWebChromeClient()
    }

    fun onRefresh() {
        webView.reload()
    }

    fun onClose() {
        finish()
    }

    fun goBack(): Boolean {
        return if (webView.canGoBack()) {
            webView.goBack()
            false
        }
        else {
            true
        }
    }

    fun loadUrl(url: String) {
        webView.loadUrl(url)
    }

    fun setTitle(title: String) {
        textView_webviewTitle.text = title
    }

    fun updateProgressBar(progress: Int) {
        if (100 > progress && View.GONE == progressBar_progress.visibility)
            progressBar_progress.visibility = View.VISIBLE
        else if (100 == progress)
            progressBar_progress.visibility = View.GONE

        progressBar_progress.progress = progress
    }

    fun startLoading() {
        progressBar_loading.visibility = View.VISIBLE
        imageView_refresh.visibility = View.INVISIBLE
    }

    fun finishLoading() {
        progressBar_loading.visibility = View.INVISIBLE
        imageView_refresh.visibility = View.VISIBLE
    }

    private inner class MocaWebChromeClient : WebChromeClient() {

        override fun onProgressChanged(webView: WebView, progress: Int) {
            updateProgressBar(progress)
        }
    }

    private inner class MocaWebViewClient : WebViewClient() {

        override fun onPageStarted(webView: WebView?, url: String?, favicon: Bitmap?) {
            startLoading()
        }

        override fun onPageFinished(webView: WebView, url: String) {
            if (url.startsWith("market://details?id=")) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)

                try {
                    this@WebViewActivity.startActivity(intent)
                }
                catch (localActivityNotFoundException: ActivityNotFoundException) {
                }
            }
            else {
                if (!isFinishing)
                    finishLoading()
            }
        }

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            val uri = Uri.parse(url)
            val lastPathSegment = uri.lastPathSegment

            if (lastPathSegment != null && lastPathSegment.endsWith(".mp4")) {
                val intent = Intent("android.intent.action.VIEW")
                intent.setDataAndType(uri, "video/*")
                webView.context.startActivity(intent)

                return true
            }
            return super.shouldOverrideUrlLoading(webView, url)
        }

    }

    companion object {
        private const val KEY_URL = "WebViewActivity.KEY_URL"
        private const val KEY_TITLE = "WebViewActivity.KEY_TITLE"

        fun goWebView(activity: Activity, url: String, title: String = "") {
            val intent = Intent(activity, WebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            intent.putExtra(KEY_TITLE, title)
            activity.startActivity(intent)
        }
    }

    private val statusBarSize: Int by lazy {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

        if (resourceId > 0)
            resources.getDimensionPixelSize(resourceId)
        else
            19
    }

}