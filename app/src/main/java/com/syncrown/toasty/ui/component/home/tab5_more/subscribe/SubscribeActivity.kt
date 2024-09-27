package com.syncrown.toasty.ui.component.home.tab5_more.subscribe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.toasty.databinding.ActivitySubscribeBinding
import com.syncrown.toasty.databinding.PopupSubscribeBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.commons.DialogCommon
import com.syncrown.toasty.ui.component.home.tab5_more.subscribe.adapter.SubscribeListAdapter

class SubscribeActivity : BaseActivity() {
    private lateinit var binding: ActivitySubscribeBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivitySubscribeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = "구독"

        setSubscribeList()
    }

    private fun setSubscribeList() {
        val arrayList = ArrayList<String>()
        arrayList.add("")
        arrayList.add("")
        arrayList.add("")
        arrayList.add("")

        binding.recyclerSubscribe.layoutManager = LinearLayoutManager(this)
        binding.recyclerSubscribe.adapter = SubscribeListAdapter(
            this,
            arrayList,
            object : SubscribeListAdapter.OnItemClickListener {
                override fun onClick(position: Int, view: View) {
                    showPopupWindow(view)
                }
            })
    }

    private fun showPopupWindow(anchor: View) {
        val popBinding = PopupSubscribeBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(
            popBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true
        )

        popBinding.menuItem1.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showSubscribeDel(supportFragmentManager, {
                //닫기
            }, {
                //삭제

            })
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(anchor, 0, 0)
    }
}