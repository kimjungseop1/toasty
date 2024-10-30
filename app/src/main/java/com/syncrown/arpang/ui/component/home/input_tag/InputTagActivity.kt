package com.syncrown.arpang.ui.component.home.input_tag

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayout
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityInputTagBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CustomDynamicTagView
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.component.home.input_tag.adapter.TagUseListAdapter

class InputTagActivity : BaseActivity() {
    private lateinit var binding: ActivityInputTagBinding
    private val resultTagList : ArrayList<String> = ArrayList()

    override fun observeViewModel() {

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

        binding.inputTagView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.addBtn.isSelected = isValidation()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.addBtn.isSelected = false
        binding.addBtn.setOnClickListener {
            if (isValidation()) {
                val position = binding.flexTagView.childCount

                val customDynamicTagView = CustomDynamicTagView(this).apply {
                    text = "# " + binding.inputTagView.text.toString()

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

        setUsedTag()

        setInterestedTag()
    }

    private fun isValidation(): Boolean {
        return binding.inputTagView.text.toString().isNotEmpty()
    }

    private fun setUsedTag() {
        val arrayList = ArrayList<String>()
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")
        //사용했던 태그
        binding.recyclerUsed.layoutManager = LinearLayoutManager(this)
        binding.recyclerUsed.adapter = TagUseListAdapter(this, arrayList)
    }

    private fun setInterestedTag() {
        val arrayList = ArrayList<String>()
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")
        //인기있는 태그
        binding.recyclerPopular.layoutManager = LinearLayoutManager(this)
        binding.recyclerPopular.adapter = TagUseListAdapter(this, arrayList)
    }
}