package com.syncrown.arpang.ui.component.home.tab4_store

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.syncrown.arpang.databinding.FragmentStoreBinding

class StoreFragment : Fragment() {
    private lateinit var binding: FragmentStoreBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoreBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.apply {
            webViewClient = object : WebViewClient() {
                // 페이지 로드 시작 시 호출
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    // 페이지 로드 시작 시 필요한 작업 수행
                }

                // 페이지 로드 완료 시 호출
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    // 페이지 로드 완료 시 필요한 작업 수행
                }

                // 페이지 로드 오류 시 호출
                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    // 오류 발생 시 처리
                }

                // 특정 URL 요청을 가로채서 다른 동작을 수행할 때 사용
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    // 필요한 경우 URL을 가로채서 처리
                    if (request?.url?.host == "www.example.com") {
                        // 특정 도메인에 대해 다른 작업 수행
                        return false // WebView에서 URL을 처리하도록 허용
                    }
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }

            settings.javaScriptEnabled = true
            loadUrl("https://www.zalson.co.kr/shop_goods/goods_list.htm?category=01000000")
        }
    }

}