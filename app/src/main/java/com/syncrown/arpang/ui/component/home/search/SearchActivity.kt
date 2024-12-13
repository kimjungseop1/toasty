package com.syncrown.arpang.ui.component.home.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.tabs.TabLayout
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivitySearchBinding
import com.syncrown.arpang.db.search_db.SearchWordDatabase
import com.syncrown.arpang.db.search_db.SearchWordEntity
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestResultMatchDto
import com.syncrown.arpang.network.model.RequestSearchingMatchDto
import com.syncrown.arpang.network.model.ResponsePopularTagDto
import com.syncrown.arpang.network.model.ResponseResultMatchDto
import com.syncrown.arpang.network.model.ResponseSearchingMatchDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CenterImageSpan
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.CustomDynamicRecommendView
import com.syncrown.arpang.ui.commons.CustomDynamicSearchView
import com.syncrown.arpang.ui.component.home.search.adapter.LibResultListAdapter
import com.syncrown.arpang.ui.component.home.search.adapter.LibSearchingListAdapter
import com.syncrown.arpang.ui.component.home.search.adapter.ShareResultListAdapter
import com.syncrown.arpang.ui.component.home.search.adapter.ShareSearchingListAdapter
import com.syncrown.arpang.ui.component.home.search.adapter.UserResultListAdapter
import com.syncrown.arpang.ui.component.home.search.adapter.UserSearchingListAdapter
import com.syncrown.arpang.ui.component.home.tab2_Lib.detail.LibDetailActivity
import com.syncrown.arpang.ui.component.home.tab3_share.detail.ShareDetailActivity
import kotlinx.coroutines.launch

class SearchActivity : BaseActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModels()

    private var currentMode: SearchMode = SearchMode.START

    private var curPage = 1
    private var curPageSize = 20

    private var isTabAdded = false

    private lateinit var userResultAdapter: UserResultListAdapter
    private lateinit var shareResultAdapter: ShareResultListAdapter
    private lateinit var libResultAdapter: LibResultListAdapter

    override fun observeViewModel() {
        lifecycleScope.launch {
            searchViewModel.getWords().observe(this@SearchActivity) { result ->
                if (result != null) {
                    Log.e("jung", "result : $result")
                    if (result.isNotEmpty()) {
                        result.forEach { searchWordEntity ->
                            setCurrentSearch(searchWordEntity.searchWord)
                        }
                    }

                    updateView()
                }
            }
        }

        lifecycleScope.launch {
            searchViewModel.popularTagResponseLiveData().observe(this@SearchActivity) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val data = result.data?.root ?: ArrayList()
                        setRecommend(data)
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

        lifecycleScope.launch {
            searchViewModel.inputMatchResponseLiveData().observe(this@SearchActivity) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val nameData = result.data?.root ?: ArrayList()
                        setUserSearch(nameData)

                        val shareData = result.data?.sub1 ?: ArrayList()
                        setShareSearch(shareData)

                        val libData = result.data?.sub2 ?: ArrayList()
                        setStorageSearch(libData)
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

        lifecycleScope.launch {
            searchViewModel.resultMatchResponseLiveData().observe(this@SearchActivity) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val nameData = result.data?.root ?: ArrayList()
                        val shareData = result.data?.sub1 ?: ArrayList()
                        val libData = result.data?.sub2 ?: ArrayList()

                        setResultView(nameData, shareData, libData)
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
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userResultAdapter = UserResultListAdapter(this, ArrayList())
        shareResultAdapter = ShareResultListAdapter(this, ArrayList())
        libResultAdapter = LibResultListAdapter(this, ArrayList())

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.deleteAllBtn.setOnClickListener {
            binding.flexSearchView.removeAllViews()
            searchViewModel.getDeleteAll()
            updateView()
        }

        setPopularTag()
        setSearchBar()

        setModeState(currentMode)


        binding.recyclerNameResult.layoutManager = LinearLayoutManager(this)
        binding.recyclerNameResult.adapter = userResultAdapter

        binding.recyclerShareResult.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerShareResult.adapter = shareResultAdapter

        binding.recyclerLibResult.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerLibResult.adapter = libResultAdapter

    }

    private fun setPopularTag() {
        searchViewModel.setPopularTag()
    }

    private fun setModeState(mode: SearchMode) {
        if (mode == SearchMode.START) {
            binding.groupState1.visibility = View.VISIBLE
            binding.groupState2.visibility = View.GONE
            binding.groupState3.visibility = View.GONE
            binding.recyclerNameResult.visibility = View.GONE
            binding.recyclerShareResult.visibility = View.GONE
            binding.recyclerLibResult.visibility = View.GONE
            updateView()
        }

        if (mode == SearchMode.SEARCHING) {
            binding.groupState1.visibility = View.GONE
            binding.flexSearchView.visibility = View.GONE
            binding.emptySearchView.visibility = View.GONE
            binding.groupState2.visibility = View.VISIBLE
            binding.groupState3.visibility = View.GONE
            binding.recyclerNameResult.visibility = View.GONE
            binding.recyclerShareResult.visibility = View.GONE
            binding.recyclerLibResult.visibility = View.GONE
        }

        if (mode == SearchMode.RESULT) {
            binding.groupState1.visibility = View.GONE
            binding.flexSearchView.visibility = View.GONE
            binding.emptySearchView.visibility = View.GONE
            binding.groupState2.visibility = View.GONE
            binding.groupState3.visibility = View.VISIBLE
            binding.recyclerNameResult.visibility = View.VISIBLE
            binding.recyclerShareResult.visibility = View.GONE
            binding.recyclerLibResult.visibility = View.GONE
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

                    setInputMatching(p0.toString())
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
                    setResultMatchList()

                    insertToDatabase(binding.actionbar.actionSearch.text.toString())

                    return true
                }
                return false
            }
        })
    }

    //TODO 내부 디비에 추가
    private fun insertToDatabase(word: String) {
        val searchWordEntity = SearchWordEntity(
            id = 0,
            searchWord = word
        )

        lifecycleScope.launch {
            val dao = SearchWordDatabase.getDatabase(applicationContext).searchWordDao()
            dao.insertOrUpdateWord(searchWordEntity)
            Log.d("jung", "Word saved")
        }
    }

    //TODO 내부 디비에서 삭제
    private fun deleteToDatabase(word: String) {
        lifecycleScope.launch {
            val dao = SearchWordDatabase.getDatabase(applicationContext).searchWordDao()
            dao.deleteWordByText(word.trim())
            Log.d("jung", "Word deleted")
        }
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
                val clickedView = it as? TextView
                val clickedText = clickedView?.text?.toString() ?: ""

                binding.flexSearchView.removeView(it)
                deleteToDatabase(clickedText)

                Log.e("jung", "delete $clickedText")
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

    private fun setRecommend(data: ArrayList<ResponsePopularTagDto.Root>) {
        binding.flexRecommendView.removeAllViews()

        for (term in data) {
            val position = binding.flexRecommendView.childCount

            val customDynamicRecommendView = CustomDynamicRecommendView(this).apply {
                text = term.hashtag_nm

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
    private fun setInputMatching(inputTxt: String) {
        val requestSearchingMatchDto = RequestSearchingMatchDto().apply {
            user_id = AppDataPref.userId
            search_nm = inputTxt
        }

        searchViewModel.inputMatchList(requestSearchingMatchDto)
    }

    private fun setUserSearch(nameData: ArrayList<ResponseSearchingMatchDto.Root>) {
        val userListAdapter = UserSearchingListAdapter(this, nameData)
        binding.recyclerUser.layoutManager = LinearLayoutManager(this)
        binding.recyclerUser.adapter = userListAdapter
        userListAdapter.setOnItemClickListener(object :
            UserSearchingListAdapter.OnItemClickListener {
            override fun onClick(position: Int, nickNm: String) {
                binding.actionbar.actionSearch.setText(nickNm)
                binding.actionbar.actionSearch.setSelection(
                    binding.actionbar.actionSearch.text?.length
                        ?: 0
                )
            }
        })
    }

    private fun setShareSearch(shareData: ArrayList<ResponseSearchingMatchDto.Sub1>) {
        val shareListAdapter = ShareSearchingListAdapter(this, shareData)
        binding.recyclerShare.layoutManager = LinearLayoutManager(this)
        binding.recyclerShare.adapter = shareListAdapter
        shareListAdapter.setOnItemClickListener(object :
            ShareSearchingListAdapter.OnItemClickListener {
            override fun onClick(position: Int, hashtagNm: String) {
                binding.actionbar.actionSearch.setText(hashtagNm)
                binding.actionbar.actionSearch.setSelection(
                    binding.actionbar.actionSearch.text?.length
                        ?: 0
                )
            }
        })
    }

    private fun setStorageSearch(libData: ArrayList<ResponseSearchingMatchDto.Sub2>) {
        val storageListAdapter = LibSearchingListAdapter(this, libData)
        binding.recyclerStorage.layoutManager = LinearLayoutManager(this)
        binding.recyclerStorage.adapter = storageListAdapter
        storageListAdapter.setOnItemClickListener(object :
            LibSearchingListAdapter.OnItemClickListener {
            override fun onClick(position: Int, hashtagNm: String) {
                binding.actionbar.actionSearch.setText(hashtagNm)
                binding.actionbar.actionSearch.setSelection(
                    binding.actionbar.actionSearch.text?.length
                        ?: 0
                )
            }
        })
    }

    /***********************************************************************************************
     * 검색 결과 - SearchMode.RESULT
     **********************************************************************************************/
    private fun setResultMatchList() {
        val requestResultMatchDto = RequestResultMatchDto().apply {
            user_id = AppDataPref.userId
            search_nm = binding.actionbar.actionSearch.text.toString()
            currPage = curPage
            pageSize = curPageSize
        }

        searchViewModel.resultMatchList(requestResultMatchDto)
    }

    private fun setResultView(
        nameData: ArrayList<ResponseResultMatchDto.Root>,
        shareData: ArrayList<ResponseResultMatchDto.Sub1>,
        libData: ArrayList<ResponseResultMatchDto.Sub2>
    ) {
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

        Log.e("jung", "isTabAdded : $isTabAdded")
        if (!isTabAdded) {
            // 사용자 탭 추가
            val userCnt = if (nameData.isEmpty()) 0 else nameData[0].user_tot_cnt
            val userTab = getString(R.string.search_step_second_desc_1_1, userCnt.toString())
            binding.customTabView.addTab(binding.customTabView.newTab().setText(userTab))
            userResultAdapter.updateData(nameData)

            // 공유공간 탭 추가
            val shareCnt = if (shareData.isEmpty()) 0 else shareData[0].contents_tot_cnt
            val shareTab = getString(R.string.search_step_second_desc_2_1, shareCnt.toString())
            binding.customTabView.addTab(binding.customTabView.newTab().setText(shareTab))
            shareResultAdapter.updateData(shareData)
            shareResultAdapter.setOnItemClickListener(object :
                ShareResultListAdapter.OnItemClickListener {
                override fun onClick(position: Int, cntntsNo: String) {
                    Log.e("jung", "공유공간 검색결과 상세페이지 이동 : $cntntsNo")
                    val intent = Intent(this@SearchActivity, ShareDetailActivity::class.java)
                    intent.putExtra("CONTENT_DETAIL_NO", cntntsNo)
                    startActivity(intent)
                }
            })

            // 보관함 탭 추가
            val libCnt = if (libData.isEmpty()) 0 else libData[0].contents_tot_cnt
            val libTab = getString(R.string.search_step_second_desc_3_1, libCnt.toString())
            binding.customTabView.addTab(binding.customTabView.newTab().setText(libTab))
            libResultAdapter.updateData(libData)
            libResultAdapter.setOnItemClickListener(object :
                LibResultListAdapter.OnItemClickListener {
                override fun onClick(position: Int, cntntsNo: String) {
                    Log.e("jung", "보관함 검색결과 상세페이지 이동 : $cntntsNo")
                    val intent = Intent(this@SearchActivity, LibDetailActivity::class.java)
                    intent.putExtra("CONTENT_DETAIL_NO", cntntsNo)
                    startActivity(intent)
                }
            })

            setTabMargins(binding.customTabView)

            binding.customTabView.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        Log.e("jung", "tabpos : ${tab.position}")
                        binding.recyclerNameResult.visibility =
                            if (tab.position == 0) View.VISIBLE else View.GONE
                        binding.recyclerShareResult.visibility =
                            if (tab.position == 1) View.VISIBLE else View.GONE
                        binding.recyclerLibResult.visibility =
                            if (tab.position == 2) View.VISIBLE else View.GONE
                    }

                    curPage = 1
                    clearData()
                    setResultMatchList()
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })

            isTabAdded = true
        } else {
            when (binding.customTabView.selectedTabPosition) {
                0 -> userResultAdapter.addData(nameData)
                1 -> shareResultAdapter.addData(shareData)
                2 -> libResultAdapter.addData(libData)
            }
        }

        setScrollListeners()
    }

    private fun clearData() {
        userResultAdapter.updateData(ArrayList())
        shareResultAdapter.updateData(ArrayList())
        libResultAdapter.updateData(ArrayList())
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

    private fun setScrollListeners() {
        if (binding.recyclerNameResult.isVisible) {
            binding.recyclerNameResult.addOnScrollListener(object : OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        curPage++
                        setResultMatchList()
                    }
                }
            })
        }

        if (binding.recyclerShareResult.isVisible) {
            binding.recyclerShareResult.addOnScrollListener(object : OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        curPage++
                        setResultMatchList()
                    }
                }
            })
        }

        if (binding.recyclerLibResult.isVisible) {
            binding.recyclerLibResult.addOnScrollListener(object : OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        curPage++
                        setResultMatchList()
                    }
                }
            })
        }
    }

    private fun clearItemDecorations(recyclerView: RecyclerView) {
        val decorationCount = recyclerView.itemDecorationCount
        for (i in decorationCount - 1 downTo 0) {
            recyclerView.removeItemDecorationAt(i)
        }
    }
}