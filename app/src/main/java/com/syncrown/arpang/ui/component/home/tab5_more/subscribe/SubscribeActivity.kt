package com.syncrown.arpang.ui.component.home.tab5_more.subscribe

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivitySubscribeBinding
import com.syncrown.arpang.databinding.PopupSubscribeBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.component.home.tab5_more.subscribe.adapter.SubscribeListAdapter
import com.syncrown.arpang.ui.component.home.tab5_more.subscribe.detail.SubscribeDetailActivity

class SubscribeActivity : BaseActivity() {
    private lateinit var binding: ActivitySubscribeBinding
    private lateinit var subscribeListAdapter: SubscribeListAdapter

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

        binding.actionbar.actionTitle.text = getString(R.string.subscribe_title)

        binding.clMyView.setOnClickListener {
            binding.clMyView.isSelected = true
            binding.clMeView.isSelected = false

            val arrayList = ArrayList<String>()
            arrayList.add("1")
            arrayList.add("1")
            arrayList.add("1")
            arrayList.add("1")

            setSubscribeList(arrayList, SubscribeType.MY)

            setSelectedViewBackground()
        }

        binding.clMeView.setOnClickListener {
            binding.clMeView.isSelected = true
            binding.clMyView.isSelected = false

            val arrayList = ArrayList<String>()
            arrayList.add("2")
            arrayList.add("2")
            arrayList.add("2")

            setSubscribeList(arrayList, SubscribeType.ME)

            setSelectedViewBackground()
        }

        val type = intent.getStringExtra("SUBSCRIBE_TYPE")
        if (type.equals(SubscribeType.ME.name)) {
            binding.clMeView.performClick()
        } else {
            binding.clMyView.performClick()
        }
    }

    private fun setSubscribeList(arrayList: ArrayList<String>, subscribeType: SubscribeType) {
        binding.recyclerSubscribe.layoutManager = LinearLayoutManager(this)
        subscribeListAdapter = SubscribeListAdapter(
            this,
            arrayList,
            subscribeType,
            object : SubscribeListAdapter.OnItemDeleteListener {
                override fun onDelete(position: Int, view: View) {
                    showPopupWindow(view, position)
                }
            },
            object : SubscribeListAdapter.OnItemClickListener {
                override fun onClick(position: Int) {
                    goDetail()
                }
            })
        binding.recyclerSubscribe.adapter = subscribeListAdapter
    }

    private fun showPopupWindow(anchor: View, position: Int) {
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
                subscribeListAdapter.removeItem(position)
            })
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(anchor, 0, 0)
    }

    private fun setSelectedViewBackground() {
        if (binding.clMyView.isSelected) {
            binding.desc1.setTextColor(ContextCompat.getColor(this, R.color.color_8e5d4b))
            binding.subscribeI.setTextColor(ContextCompat.getColor(this, R.color.color_8e5d4b))
        } else {
            binding.desc1.setTextColor(ContextCompat.getColor(this, R.color.color_black))
            binding.subscribeI.setTextColor(ContextCompat.getColor(this, R.color.color_black))
        }

        if (binding.clMeView.isSelected) {
            binding.desc2.setTextColor(ContextCompat.getColor(this, R.color.color_8e5d4b))
            binding.subscribeMe.setTextColor(ContextCompat.getColor(this, R.color.color_8e5d4b))
        } else {
            binding.desc2.setTextColor(ContextCompat.getColor(this, R.color.color_black))
            binding.subscribeMe.setTextColor(ContextCompat.getColor(this, R.color.color_black))
        }
    }

    private fun goDetail() {
        val intent = Intent(this, SubscribeDetailActivity::class.java)
        startActivity(intent)
    }
}