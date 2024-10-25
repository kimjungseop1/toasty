package com.syncrown.arpang.ui.component.home.tab2_Lib.detail

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayout
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityLibDetailBinding
import com.syncrown.arpang.databinding.PopupLibDeleteBinding
import com.syncrown.arpang.databinding.PopupLibReportBinding
import com.syncrown.arpang.databinding.PopupMenuDetailBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CustomDynamicTagView
import com.syncrown.arpang.ui.commons.DialogCommon
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

        }

        binding.actionbar.actionMore.setOnClickListener {
            showPopupWindow(binding.actionbar.actionMore)
        }

        showFlexTagView()

        showCommentView()

        showEditView()
    }

    private fun showFlexTagView() {
        val position = binding.flexTagView.childCount
        val customDynamicTagView = CustomDynamicTagView(this).apply {
            text = "# (get data)"

            tag = position
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

    private fun showPopupWindow(anchor: View) {
        val popBinding = PopupMenuDetailBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(popBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true)

        popBinding.switchMenu1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 스위치가 켜졌을 때
            } else {
                // 스위치가 꺼졌을 때
            }
        }

        popBinding.menuItem2.setOnClickListener {
            popupWindow.dismiss()
        }

        popBinding.menuItem3.setOnClickListener {
            popupWindow.dismiss()
        }

        popBinding.menuItem4.setOnClickListener {
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(anchor, 0, 0)
    }

    private fun showCommentView() {
        val arrayList = ArrayList<String>()
        arrayList.add("0")
        arrayList.add("1")
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
}