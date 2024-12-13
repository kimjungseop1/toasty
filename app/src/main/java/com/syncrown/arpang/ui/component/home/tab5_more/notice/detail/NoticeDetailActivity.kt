package com.syncrown.arpang.ui.component.home.tab5_more.notice.detail

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Base64
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityNoticeDetailBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestNoticeDetailDto
import com.syncrown.arpang.network.model.ResponseNoticeDetailDto
import com.syncrown.arpang.ui.base.BaseActivity
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream

class NoticeDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityNoticeDetailBinding
    private val noticeDetailViewModel: NoticeDetailViewModel by viewModels()

    var mBbsid = ""

    override fun observeViewModel() {
        lifecycleScope.launch {
            noticeDetailViewModel.noticeDetailResponseLiveData()
                .observe(this@NoticeDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.root
                            data?.let {
                                updateUi(it)
                            }
                        }

                        is NetworkResult.NetCode -> {
                            Log.e("jung", "실패 : ${result.message}")
                            if (result.message.equals("403")) {
                                goLogin()
                            }
                        }

                        is NetworkResult.Error -> {
                            Log.e("jung", "오류 : ${result.message}")
                        }
                    }
                }
        }
    }

    override fun initViewBinding() {
        binding = ActivityNoticeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBbsid = intent.getStringExtra("NOTICE_DETAIL_BBSID").toString()

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.notice_title)

        setNoticeDetail()
    }

    private fun setNoticeDetail() {
        val requestNoticeDetailDto = RequestNoticeDetailDto().apply {
            bbsid = mBbsid
        }

        noticeDetailViewModel.noticeListDetail(requestNoticeDetailDto)
    }

    private fun updateUi(data: ResponseNoticeDetailDto.Root) {
        //제목
        binding.contentView.text = data.title

        //게시일
        binding.dateView.text = data.start_dt

        //본문
        data.content?.let {
            setHtmlContent(binding.bodyTxt, it.trimIndent())
        }
    }

    private fun setHtmlContent(textView: TextView, content: String) {
        val spanned: Spanned = HtmlCompat.fromHtml(
            content,
            HtmlCompat.FROM_HTML_MODE_LEGACY,
            { source ->
                if (source != null && source.startsWith("data:image/")) {
                    val base64Data = source.substringAfter("base64,")
                    try {
                        val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
                        val inputStream = ByteArrayInputStream(decodedBytes)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        val drawable = BitmapDrawable(textView.context.resources, bitmap)
                        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                        drawable
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                } else {
                    null
                }
            },
            null
        )
        textView.text = spanned
    }
}