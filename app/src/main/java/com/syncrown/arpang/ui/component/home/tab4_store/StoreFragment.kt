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
import androidx.activity.addCallback
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
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                }
            }

            settings.javaScriptEnabled = true
            loadUrl("https://www.zalson.co.kr/shop_goods/goods_list.htm?category=01000000")
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}
