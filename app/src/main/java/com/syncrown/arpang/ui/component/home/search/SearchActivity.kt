package com.syncrown.arpang.ui.component.home.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivitySearchBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CenterImageSpan
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.CustomDynamicRecommendView
import com.syncrown.arpang.ui.commons.CustomDynamicSearchView
import com.syncrown.arpang.ui.component.home.search.adapter.SearchResultPagerAdapter
import com.syncrown.arpang.ui.component.home.search.adapter.SearchingListAdapter

class SearchActivity : BaseActivity() {
    private lateinit var binding: ActivitySearchBinding
    private var currentMode : SearchMode = SearchMode.START

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
            updateView()
        }

        setSearchBar()
        setRecommend()

        setModeState(currentMode)
    }

    private fun setModeState(mode: SearchMode) {
        if (mode == SearchMode.START) {
            binding.groupState1.visibility = View.VISIBLE
            binding.groupState2.visibility = View.GONE
            binding.groupState3.visibility = View.GONE

            updateView()
        }

        if (mode == SearchMode.SEARCHING) {
            binding.groupState1.visibility = View.GONE
            binding.emptySearchView.visibility = View.GONE
            binding.groupState2.visibility = View.VISIBLE
            binding.groupState3.visibility = View.GONE
        }

        if (mode == SearchMode.RESULT) {
            binding.groupState1.visibility = View.GONE
            binding.emptySearchView.visibility = View.GONE
            binding.groupState2.visibility = View.GONE
            binding.groupState3.visibility = View.VISIBLE
        }
    }

    /***********************************************************************************************
     * 검색 전 - SearchMode.START
     **********************************************************************************************/
    private fun setSearchBar() {
        binding.actionbar.actionSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.isEmpty() == true) {
                    currentMode = SearchMode.START
                    setModeState(currentMode)
                    binding.actionbar.actionClear.visibility = View.GONE
                } else {
                    currentMode = SearchMode.SEARCHING
                    setModeState(currentMode)
                    binding.actionbar.actionClear.visibility = View.VISIBLE

                    setSearchingView()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.actionbar.actionClear.setOnClickListener {
            if (currentMode == SearchMode.RESULT) {
                setCurrentSearch(binding.actionbar.actionSearch.text.toString())
            }
            binding.actionbar.actionSearch.text?.clear()
        }

        binding.actionbar.actionSearch.setOnEditorActionListener(object : OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    currentMode = SearchMode.RESULT
                    setModeState(currentMode)
                    setResultView()

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
    private fun setCurrentSearch(inputTxt: String) {
        if (isValid()) {
            val position = binding.flexSearchView.childCount

            val customDynamicSearchView = CustomDynamicSearchView(this).apply {
                val enteredText = inputTxt.trim()

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

    private fun setRecommend() {
        val arrayList = ArrayList<String>()
        arrayList.add("추천1")
        arrayList.add("추천22")
        arrayList.add("추천333")
        arrayList.add("추천4444")
        arrayList.add("추천55555")

        binding.flexRecommendView.removeAllViews()

        for (term in arrayList) {
            val position = binding.flexRecommendView.childCount

            val customDynamicRecommendView = CustomDynamicRecommendView(this).apply {
                text = term

                tag = position

                setOnClickListener { _ ->
                    binding.actionbar.actionSearch.setText(text)
                    binding.actionbar.actionSearch.setSelection(text.length)
                    binding.actionbar.actionSearch.requestFocus()
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

    /***********************************************************************************************
     * 검색 중 - SearchMode.SEARCHING
     **********************************************************************************************/
    private fun setSearchingView() {
        setUserSearch()

        setShareSearch()

        setStorageSearch()
    }

    private fun setUserSearch() {
        val arrayList = ArrayList<String>()
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")

        val userListAdapter = SearchingListAdapter(this, arrayList)
        binding.recyclerUser.layoutManager = LinearLayoutManager(this)
        binding.recyclerUser.adapter = userListAdapter
        userListAdapter.setOnItemClickListener(object : SearchingListAdapter.OnItemClickListener {
            override fun onClick(position: Int) {

            }
        })
    }

    private fun setShareSearch() {
        val arrayList = ArrayList<String>()
        arrayList.add("2")
        arrayList.add("2")
        arrayList.add("2")

        val shareListAdapter = SearchingListAdapter(this, arrayList)
        binding.recyclerShare.layoutManager = LinearLayoutManager(this)
        binding.recyclerShare.adapter = shareListAdapter
        shareListAdapter.setOnItemClickListener(object : SearchingListAdapter.OnItemClickListener {
            override fun onClick(position: Int) {

            }
        })
    }

    private fun setStorageSearch() {
        val arrayList = ArrayList<String>()
        arrayList.add("3")
        arrayList.add("3")

        val storageListAdapter = SearchingListAdapter(this, arrayList)
        binding.recyclerStorage.layoutManager = LinearLayoutManager(this)
        binding.recyclerStorage.adapter = storageListAdapter
        storageListAdapter.setOnItemClickListener(object :
            SearchingListAdapter.OnItemClickListener {
            override fun onClick(position: Int) {

            }
        })
    }

    /***********************************************************************************************
     * 검색 결과 - SearchMode.RESULT
     **********************************************************************************************/
    private fun setResultView() {
        val actionSearchText = binding.actionbar.actionSearch.text.toString().trim()
        val fullText = getString(R.string.search_step_third_desc_1, actionSearchText)

        val spannableString = SpannableString(fullText)
        val startIndex = fullText.indexOf(actionSearchText)
        val endIndex = startIndex + actionSearchText.length

        if (startIndex >= 0) {
            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_8e5d4b)),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.desc6.text = spannableString

        binding.pagerView.adapter = SearchResultPagerAdapter(this)
        binding.pagerView.isUserInputEnabled = false
        TabLayoutMediator(binding.customTabView, binding.pagerView) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.search_step_second_desc_1_1, "1111")
                }

                1 -> {
                    tab.text = getString(R.string.search_step_second_desc_2_1, "22222")
                }

                2 -> {
                    tab.text = getString(R.string.search_step_second_desc_3_1, "33333")
                }
            }
        }.attach()
        setTabMargins(binding.customTabView)
    }

    private fun setTabMargins(tabLayout: TabLayout) {
        val tabStrip = tabLayout.getChildAt(0) as ViewGroup
        for (i in 0 until tabStrip.childCount) {
            val tab = tabStrip.getChildAt(i)
            val layoutParams = tab.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.marginEnd = CommonFunc.dpToPx(5, this)
            tab.requestLayout()
        }
    }
}