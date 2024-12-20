package com.syncrown.arpang.ui.component.join.term_privacy

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityWebPolishBinding
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.ui.base.BaseActivity
import kotlinx.coroutines.launch

class PolishWebActivity : BaseActivity() {
    private lateinit var binding: ActivityWebPolishBinding
    private lateinit var baseUrl: String
    private var type: String = ""
    private var preActivity: Int = 0

    override fun observeViewModel() {
        lifecycleScope.launch {

        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initViewBinding() {
        binding = ActivityWebPolishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        type = intent.getStringExtra("CONSENT_EXTRA").toString()

        //회원가입화면에서의 진입인지, 약관동의화면에서의 진입인지 체크
        preActivity = intent.getIntExtra("FROM_JOIN_EMAIL", 0)

        when (type) {
            "term" -> {
                binding.actionbar.actionTitle.text =
                    ContextCompat.getString(this, R.string.polish_term_title)
                binding.consentBtn.text = ContextCompat.getString(this, R.string.polish_term_btn)

                baseUrl = ArPangRepository.BASE_URL_DEV + "/ntv/mypagee/clauses"

            }

            "privacy" -> {
                binding.actionbar.actionTitle.text =
                    ContextCompat.getString(this, R.string.polish_privacy_title)
                binding.consentBtn.text = ContextCompat.getString(this, R.string.polish_privacy_btn)

                baseUrl = ArPangRepository.BASE_URL_DEV + "/ntv/mypagee/privacy"
            }
        }

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

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }

            settings.javaScriptEnabled = true
            loadUrl(baseUrl)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (preActivity == 0) {
            binding.clView.visibility = View.GONE
        } else {
            binding.clView.visibility = View.VISIBLE
        }

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.consentBtn.setOnClickListener {
            finish()
        }
    }
}