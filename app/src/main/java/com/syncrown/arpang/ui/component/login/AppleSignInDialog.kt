package com.syncrown.arpang.ui.component.login

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import java.util.*

class AppleSignInDialog(context: Context, val interaction: Interaction? = null) : Dialog(context) {
    interface Interaction {
        fun onAppleEmailSuccess(sub: String)
    }

    companion object {
        const val APPLE_CLIENT_ID = "com.syncrown.arpang3" // 사이트의 Certificates, Identifiers & Profiles 의 IDENTIFIER 값
        const val APPLE_REDIRECT_URI = "https://www.zalson.co.kr/API/naver/login_exec.php" // 잘쓴앱꺼 일단 가져옴
        const val APPLE_SCOPE = "name%20email"
        const val APPLE_AUTHURL = "https://appleid.apple.com/auth/authorize"
    }

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(context)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = AppleWebViewClient()
        webView.loadUrl(
            APPLE_AUTHURL
                    + "?response_type=code&v=1.1.6&response_mode=form_post&client_id="
                    + APPLE_CLIENT_ID + "&scope="
                    + APPLE_SCOPE + "&state="
                    + UUID.randomUUID().toString() + "&redirect_uri="
                    + APPLE_REDIRECT_URI
        )
        setContentView(webView)
    }

    inner class AppleWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            view?.loadUrl(request?.url.toString())
            return true
        }

        // For API 19 and below
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }


        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            url?.let {
                val uri = Uri.parse(it)
                //status
                uri.getQueryParameter("status")?.let { status ->
                    if (status == "fail") {
                        Toast.makeText(context, "로그인에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                            .show()
                        dismiss()
                    } else if (status == "success") {
                        uri.getQueryParameter("email")?.let { email ->
                            interaction?.onAppleEmailSuccess(email) //sub rather than email
                            dismiss()
                        }
                    }
                }
            }
            val displayRectangle = Rect()
            val window = window
            window?.decorView?.getWindowVisibleDisplayFrame(displayRectangle)

            // Set height of the Dialog to 90% of the screen
            val layoutParams = view?.layoutParams
            layoutParams?.height = (displayRectangle.height() * 0.9f).toInt()
            view?.layoutParams = layoutParams
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}