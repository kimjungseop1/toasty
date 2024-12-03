package com.syncrown.arpang.ui.component.home.input_tag

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayout
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityInputTagBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestAllFavoriteDto
import com.syncrown.arpang.network.model.RequestMyFavoriteDto
import com.syncrown.arpang.network.model.ResponseAllFavoriteDto
import com.syncrown.arpang.network.model.ResponseMyFavoriteDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CustomDynamicTagView
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.component.home.input_tag.adapter.TagAllFavoriteListAdapter
import com.syncrown.arpang.ui.component.home.input_tag.adapter.TagUseListAdapter
import kotlinx.coroutines.launch

class InputTagActivity : BaseActivity() {
    private lateinit var binding: ActivityInputTagBinding
    private val inputTagViewModel: InputTagViewModel by viewModels()

    private val resultTagList: ArrayList<String> = ArrayList()

    override fun observeViewModel() {
        lifecycleScope.launch {
            inputTagViewModel.allFavoriteHashTagResponseLiveData()
                .observe(this@InputTagActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data: ArrayList<ResponseAllFavoriteDto.Root>? = result.data?.root
                            data?.let {
                                updateAllFavoriteUI(it)
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

        lifecycleScope.launch {
            inputTagViewModel.myFavoriteHashTagResponseLiveData()
                .observe(this@InputTagActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data: ArrayList<ResponseMyFavoriteDto.Root>? = result.data?.root
                            data?.let {
                                updateMyUsedTagUI(data)
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
        binding = ActivityInputTagBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }
        binding.actionbar.actionTitle.text = getString(R.string.tag_popup_input)
        binding.actionbar.actionEtc.text = getString(R.string.tag_popup_submit)
        binding.actionbar.actionEtc.setOnClickListener {
            TagResultListStorage.tagArrayList = resultTagList
            setResult(RESULT_OK)
            finish()
        }

        showTagViewState()

        binding.inputTagView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.addBtn.isSelected = isValidation()

                setMyFavoriteHashTag(p0.toString())
                setAllFavoriteHashTag(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.addBtn.isSelected = false
        binding.addBtn.setOnClickListener {
            if (isValidation()) {
                addTagView(binding.inputTagView.text.toString())

                hideKeyBoard()
            }
        }
    }

    private fun showTagViewState() {
        binding.root.isKeyboardVisible { isVisible ->
            if (isVisible) {
                binding.groupTitle.visibility = View.GONE
                binding.groupPopular.visibility = View.VISIBLE
            } else {
                binding.groupTitle.visibility = View.VISIBLE
                binding.groupPopular.visibility = View.GONE
            }
        }
    }

    private fun isValidation(): Boolean {
        return binding.inputTagView.text.toString().isNotEmpty()
    }

    private fun setMyFavoriteHashTag(searchTxt: String) {
        val requestMyFavoriteDto = RequestMyFavoriteDto()
        requestMyFavoriteDto.search_nm = searchTxt
        inputTagViewModel.myFavoriteHashTag(requestMyFavoriteDto)
    }

    private fun updateMyUsedTagUI(data: ArrayList<ResponseMyFavoriteDto.Root>) {
        //사용했던 태그
        val adapter = TagUseListAdapter(this, data)
        binding.recyclerUsed.layoutManager = LinearLayoutManager(this)
        binding.recyclerUsed.adapter = adapter
        adapter.setOnItemClickListener(object : TagUseListAdapter.OnItemClickListener {
            override fun onClick(position: Int, text: String) {
                addTagView(text)
            }
        })
    }

    private fun setAllFavoriteHashTag(searchTxt: String) {
        val requestAllFavoriteDto = RequestAllFavoriteDto()
        requestAllFavoriteDto.search_nm = searchTxt
        inputTagViewModel.allFavoriteHashTag(requestAllFavoriteDto)
    }

    private fun updateAllFavoriteUI(data: ArrayList<ResponseAllFavoriteDto.Root>) {
        //인기있는 태그
        val adapter = TagAllFavoriteListAdapter(this, data)
        binding.recyclerPopular.layoutManager = LinearLayoutManager(this)
        binding.recyclerPopular.adapter = adapter
        adapter.setOnItemClickListener(object : TagAllFavoriteListAdapter.OnItemClickListener {
            override fun onClick(position: Int, text: String) {
                addTagView(text)
            }
        })
    }

    private fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let { view ->
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun View.isKeyboardVisible(listener: (Boolean) -> Unit) {
        val rootView = this.rootView
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            private var isKeyboardVisible = false

            override fun onGlobalLayout() {
                val rect = Rect()
                rootView.getWindowVisibleDisplayFrame(rect)

                val screenHeight = rootView.height
                val keypadHeight = screenHeight - rect.bottom

                val isVisible = keypadHeight > screenHeight * 0.15

                if (isVisible != isKeyboardVisible) {
                    isKeyboardVisible = isVisible
                    listener(isKeyboardVisible)
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun addTagView(tagTxt: String) {
        val position = binding.flexTagView.childCount

        val customDynamicTagView = CustomDynamicTagView(this).apply {
            text = "# $tagTxt"

            tag = position

            resultTagList.add(text.toString())

            setOnClickListener {
                val clickedView = it

                val dialogCommon = DialogCommon()
                dialogCommon.showTagDelete(supportFragmentManager, {
                    //nothing
                }, {
                    binding.flexTagView.removeView(clickedView)
                })
            }
        }

        val layoutParams = FlexboxLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(5, 5, 5, 5)
        }

        customDynamicTagView.layoutParams = layoutParams
        binding.flexTagView.addView(customDynamicTagView)
        binding.inputTagView.setText("")
    }
}