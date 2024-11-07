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
        fun onAppleIdSuccess(sub: String)
    }

    companion object {
        const val APPLE_CLIENT_ID = "com.syncrown.arpang3" // 사이트의 Certificates, Identifiers & Profiles 의 IDENTIFIER 값
        const val APPLE_REDIRECT_URI = "https://www.zalson.co.kr/API/naver/login_exec.php" // 잘쓴앱꺼 일단 가져옴
        const val APPLE_SCOPE = "name%20email"
        const val APPLE_AUTH_URL = "https://appleid.apple.com/auth/authorize"
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
            APPLE_AUTH_URL
                    + "?response_type=code&v=1.1.6&response_mode=form_post&client_id="
                    + APPLE_CLIENT_ID + "&scope="
                    + APPLE_SCOPE + "&state="
                    + UUID.randomUUID().toString() + "&redirect_uri="
                    + APPLE_REDIRECT_URI
        )

        setContentView(webView)
    }

    inner class AppleWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            request?.url?.let { url ->
                if (url.host == "appleid.apple.com") { // 애플의 도메인인지 확인
                    view?.loadUrl(url.toString())
                    return true
                }
            }
            return false
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            url?.let {
                val uri = Uri.parse(it)
                uri.getQueryParameter("status")?.let { status ->
                    if (status == "fail") {
                        Toast.makeText(context, "로그인에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else if (status == "success") {
                        uri.getQueryParameter("sub")?.let { sub ->
                            interaction?.onAppleIdSuccess(sub)
                            dismiss()
                        }
                    }
                }
            }

            val displayRectangle = Rect()
            window?.decorView?.getWindowVisibleDisplayFrame(displayRectangle)

            view?.let {
                val layoutParams = it.layoutParams
                layoutParams?.height = (displayRectangle.height() * 0.9f).toInt()
                it.layoutParams = layoutParams
                it.requestLayout() // 레이아웃 변경 요청
            }
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