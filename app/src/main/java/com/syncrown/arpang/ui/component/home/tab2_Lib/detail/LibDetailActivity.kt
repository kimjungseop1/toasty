package com.syncrown.arpang.ui.component.home.tab2_Lib.detail

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityLibDetailBinding
import com.syncrown.arpang.databinding.BottomSheetAnotherPaperBinding
import com.syncrown.arpang.databinding.BottomSheetCartridgeBinding
import com.syncrown.arpang.databinding.BottomSheetPrinterDisconnectBinding
import com.syncrown.arpang.databinding.PopupLibDeleteBinding
import com.syncrown.arpang.databinding.PopupLibReportBinding
import com.syncrown.arpang.databinding.PopupMenuDetailBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CustomDynamicTagView
import com.syncrown.arpang.ui.commons.CustomToast
import com.syncrown.arpang.ui.commons.CustomToastType
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.commons.DialogToastingCommon
import com.syncrown.arpang.ui.component.home.input_tag.InputTagActivity
import com.syncrown.arpang.ui.component.home.input_tag.TagResultListStorage
import com.syncrown.arpang.ui.component.home.tab3_share.detail.adapter.DetailCommentListAdapter

class LibDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityLibDetailBinding
    private lateinit var detailCommentListAdapter: DetailCommentListAdapter

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityLibDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.home_nav_title_2)

        binding.actionbar.actionEtc1.text = getString(R.string.actionbar_print_text)
        binding.actionbar.actionEtc1.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this,
                R.drawable.icon_action_print
            ), null, null, null
        )
        binding.actionbar.actionEtc1.setOnClickListener {
            //TODO 프린터 연결 + 용지 장착
            setPrinterAndPaper()

            //TODO 프린터 연결 + 용지 불일치
            setPrinterAndNotPaper()

            //TODO 프린터 미연동
            setDisconnectPrinter()

            //TODO 프린터 연결 + 용지 미장착
            setPrinterAndAnotherPaper()
        }

        binding.actionbar.actionMore.setOnClickListener {
            showPopupWindow(binding.actionbar.actionMore)
        }

        TagResultListStorage.tagArrayList?.let {
            showFlexTagView(it)
        }

        showCommentView()

        binding.inputComment.isEnabled = false
        if (binding.inputComment.isEnabled) {
            binding.inputComment.hint = getString(R.string.storage_detail_input_hint)
        } else {
            binding.inputComment.hint = getString(R.string.storage_detail_input_hint_disable)
        }
        showEditView()
    }

    override fun onDestroy() {
        super.onDestroy()
        TagResultListStorage.tagArrayList = null
    }

    private fun showFlexTagView(data : ArrayList<String>) {
        for (i in 0 until data.size) {
            val customDynamicTagView = CustomDynamicTagView(this).apply {
                text = data[i]
                tag = i
            }

            val layoutParams = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(5, 5, 5, 5)
            }

            customDynamicTagView.layoutParams = layoutParams
            binding.flexTagView.addView(customDynamicTagView)
        }
    }

    private fun showPopupWindow(anchor: View) {
        val popBinding = PopupMenuDetailBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(
            popBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true
        )

        popBinding.switchMenu1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 스위치가 켜졌을 때

                // 공개글로 설정할수없을때 메시지
                val customToast = CustomToast()
                customToast.showToastMessage(
                    supportFragmentManager,
                    getString(R.string.storage_detail_more_tab_nondisclosure_error_toast),
                    CustomToastType.RED
                ) {
                    //닫기
                }
            } else {
                // 스위치가 꺼졌을 때
                val dialogCommon = DialogCommon()
                dialogCommon.showLibDetailNondisclosure(supportFragmentManager, {
                    //닫기
                }, {
                    //비공개

                })
            }
        }

        popBinding.menuItem2.setOnClickListener {
            popupWindow.dismiss()
            goInputTag()
        }

        popBinding.menuItem3.setOnClickListener {
            popupWindow.dismiss()
        }

        popBinding.menuItem4.setOnClickListener {
            popupWindow.dismiss()
            val dialogCommon = DialogCommon()
            dialogCommon.showCommentDelete(supportFragmentManager, {
                //닫기
            }, {
                //삭제
            })
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(anchor, 0, 0)
    }

    private fun showCommentView() {
        val arrayList = ArrayList<String>()
        arrayList.add("0")
        arrayList.add("1")
        arrayList.add("1")

        binding.recyclerComment.layoutManager = LinearLayoutManager(this)
        detailCommentListAdapter = DetailCommentListAdapter(
            this,
            arrayList,
            object : DetailCommentListAdapter.OnItemClickListener {
                override fun onClick(position: Int, view: View) {
                    if (arrayList[position] == "0") {
                        //본인댓글 삭제하기
                        showPopupDeleteMyComment(view, position)
                    } else {
                        //남의댓글 신고하기
                        showPopupReportOthers(view, position)
                    }
                }
            })
        binding.recyclerComment.adapter = detailCommentListAdapter
    }

    private fun showPopupDeleteMyComment(view: View, position: Int) {
        val popBinding = PopupLibDeleteBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(
            popBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true
        )

        popBinding.menuItem1.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showCommentDelete(supportFragmentManager, {
                //닫기
            }, {
                //삭제
                detailCommentListAdapter.removeItem(position)
            })
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(view, 0, 0)
    }

    private fun showPopupReportOthers(view: View, position: Int) {
        val popBinding = PopupLibReportBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(
            popBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true
        )

        popBinding.menuItem1.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showCommentReport(supportFragmentManager, {
                //닫기
            }, {
                //신고
                //detailCommentListAdapter.removeItem(position)
                val customToast = CustomToast()
                customToast.showToastMessage(
                    supportFragmentManager,
                    getString(R.string.lib_popup_report_comment),
                    CustomToastType.WHITE
                ) {
                    //close
                }
            })
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(view, 0, 0)
    }

    private fun showEditView() {
        binding.root.isKeyboardVisible { isVisible ->
            if (isVisible) {
                val newHeight = resources.getDimensionPixelSize(R.dimen.new_edit_text_height)
                binding.inputComment.layoutParams.height = newHeight
                binding.inputComment.requestLayout()

                // 전송 버튼 보이기
                binding.sendBtn.visibility = View.VISIBLE
            } else {
                val originalHeight =
                    resources.getDimensionPixelSize(R.dimen.original_edit_text_height)
                binding.inputComment.layoutParams.height = originalHeight
                binding.inputComment.requestLayout()

                // 전송 버튼 숨기기
                binding.sendBtn.visibility = View.GONE
            }
        }

        binding.sendBtn.setOnClickListener {

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

    private fun setPrinterAndPaper() {
        val binding = BottomSheetCartridgeBinding.inflate(layoutInflater)
        val bottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setContentView(binding.root)

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.isDraggable = false
        behavior.isHideable = false

        binding.concentration1.isSelected = true
        binding.printType1.isSelected = true
        binding.onePaper.isSelected = true

        binding.concentration1.setOnClickListener {
            binding.concentration1.isSelected = true
            binding.concentration2.isSelected = false
            binding.concentration3.isSelected = false
        }

        binding.concentration2.setOnClickListener {
            binding.concentration1.isSelected = false
            binding.concentration2.isSelected = true
            binding.concentration3.isSelected = false
        }

        binding.concentration3.setOnClickListener {
            binding.concentration1.isSelected = false
            binding.concentration2.isSelected = false
            binding.concentration3.isSelected = true
        }

        binding.printType1.setOnClickListener {
            binding.printType1.isSelected = true
            binding.printType2.isSelected = false
        }

        binding.printType2.setOnClickListener {
            binding.printType1.isSelected = false
            binding.printType2.isSelected = true
        }

        binding.onePaper.setOnClickListener {
            binding.onePaper.isSelected = true
            binding.twoPaper.isSelected = false
        }

        binding.twoPaper.setOnClickListener {
            binding.onePaper.isSelected = false
            binding.twoPaper.isSelected = true
        }

        binding.closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.submitBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
            showToasting()
        }

        bottomSheetDialog.show()
    }

    private fun setPrinterAndAnotherPaper() {
        val customToast = CustomToast()
        customToast.showToastMessage(
            supportFragmentManager,
            getString(R.string.lib_detail_popup_paper_not),
            CustomToastType.WHITE
        ) {
            //close
        }
//        val binding = BottomSheetPaperDisconnectBinding.inflate(layoutInflater)
//        val bottomSheetDialog =
//            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
//        bottomSheetDialog.window?.setDimAmount(0.7f)
//        bottomSheetDialog.setContentView(binding.root)
//
//        val bottomSheet =
//            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//        val behavior = BottomSheetBehavior.from(bottomSheet!!)
//        behavior.isDraggable = false
//        behavior.isHideable = false
//
//        binding.closeBtn.setOnClickListener {
//            bottomSheetDialog.dismiss()
//        }
//
//        binding.submitBtn.text = getString(R.string.tag_popup_left_btn)
//        binding.submitBtn.setOnClickListener {
//            bottomSheetDialog.dismiss()
//        }
//
//        bottomSheetDialog.show()
    }

    private fun setDisconnectPrinter() {
        val binding = BottomSheetPrinterDisconnectBinding.inflate(layoutInflater)
        val bottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setContentView(binding.root)

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.isDraggable = false
        behavior.isHideable = false

        binding.closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.submitBtn.text = getString(R.string.tag_popup_left_btn)
        binding.submitBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun setPrinterAndNotPaper() {
        val binding = BottomSheetAnotherPaperBinding.inflate(layoutInflater)
        val bottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setContentView(binding.root)

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.isDraggable = false
        behavior.isHideable = false

        Glide.with(this)
            .load(R.drawable.sample_img_1)
            .into(binding.contentResultImg)

        binding.closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.nextBtn.text = getString(R.string.tag_popup_left_btn_1)
        binding.nextBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.submitBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
            showToasting()
        }

        bottomSheetDialog.show()
    }

    private fun showToasting() {
        val dialogToast = DialogToastingCommon()
        dialogToast.showLoading(supportFragmentManager)
    }

    private fun goInputTag() {
        val intent = Intent(this, InputTagActivity::class.java)
        tagEditLauncher.launch(intent)
    }

    private val tagEditLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                TagResultListStorage.tagArrayList?.let {
                    showFlexTagView(it)
                }
            }
        }
}