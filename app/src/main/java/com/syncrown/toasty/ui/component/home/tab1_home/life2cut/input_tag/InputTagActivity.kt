package com.syncrown.toasty.ui.component.home.tab1_home.life2cut.input_tag

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import com.google.android.flexbox.FlexboxLayout
import com.syncrown.toasty.databinding.ActivityInputTagBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.commons.CustomDynamicTagView
import com.syncrown.toasty.ui.commons.DialogCommon

class InputTagActivity : BaseActivity() {
    private lateinit var binding: ActivityInputTagBinding

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
        binding.actionbar.actionTitle.text = "태그 입력"
        binding.actionbar.actionEtc.text = "완료"
        binding.actionbar.actionEtc.setOnClickListener {

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

    }

    private fun isValidation():Boolean {
        return binding.inputTagView.text.toString().isNotEmpty()
    }
}