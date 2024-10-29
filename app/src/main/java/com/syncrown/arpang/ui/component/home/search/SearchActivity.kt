package com.syncrown.arpang.ui.component.home.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivitySearchBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CenterImageSpan
import com.syncrown.arpang.ui.commons.CustomDynamicRecommendView
import com.syncrown.arpang.ui.commons.CustomDynamicSearchView

class SearchActivity : BaseActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.deleteAllBtn.setOnClickListener {
            binding.flexSearchView.removeAllViews()
        }

        setSearchBar()

        updateView()

        val arrayList = ArrayList<String>()
        arrayList.add("추천1")
        arrayList.add("추천22")
        arrayList.add("추천333")
        arrayList.add("추천4444")
        arrayList.add("추천55555")
        setRecommend(arrayList)
    }

    private fun setSearchBar() {
        binding.actionbar.actionSearch.setOnEditorActionListener(object : OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    setCurrentSearch()
                    return true
                }
                return false
            }
        })
    }

    private fun updateView() {
        if (binding.flexSearchView.childCount == 0) {
            binding.emptySearchView.visibility = View.VISIBLE
            binding.flexSearchView.visibility = View.INVISIBLE
        } else {
            binding.emptySearchView.visibility = View.INVISIBLE
            binding.flexSearchView.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCurrentSearch() {
        if (isValid()) {
            val position = binding.flexSearchView.childCount

            val customDynamicSearchView = CustomDynamicSearchView(this).apply {
                val enteredText = binding.actionbar.actionSearch.text.toString().trim()

                val drawable = ContextCompat.getDrawable(context, R.drawable.search_tag_delete)
                drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

                val spannableString = SpannableString("$enteredText  ")

                drawable?.let {
                    val imageSpan = CenterImageSpan(it)
                    spannableString.setSpan(
                        imageSpan,
                        spannableString.length - 1,
                        spannableString.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                text = spannableString
                tag = position

                setOnClickListener {
                    binding.flexSearchView.removeView(it)
                    updateView()
                }
            }

            val layoutParams = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(5, 10, 5, 10)
            }

            customDynamicSearchView.layoutParams = layoutParams
            binding.flexSearchView.addView(customDynamicSearchView)

            binding.actionbar.actionSearch.setText("")
        }

        updateView()
    }

    private fun isValid(): Boolean {
        return binding.actionbar.actionSearch.text.toString().isNotEmpty()
    }

    private fun setRecommend(arrayList: ArrayList<String>) {
        binding.flexRecommendView.removeAllViews()

        for (term in arrayList) {
            val position = binding.flexRecommendView.childCount

            val customDynamicRecommendView = CustomDynamicRecommendView(this).apply {
                text = term

                tag = position

                setOnClickListener {
                    val clickedView = it

                }
            }

            val layoutParams = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(5, 10, 5, 10)
            }

            customDynamicRecommendView.layoutParams = layoutParams
            binding.flexRecommendView.addView(customDynamicRecommendView)
        }
    }
}