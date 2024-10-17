package com.syncrown.arpang.ui.component.home.tab1_home.ar_print.guide

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityArprintGuideBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.guide.adapter.ArGuidePagerAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videoselect.VideoSelectActivity

class ArGuideActivity : BaseActivity() {
    private lateinit var binding: ActivityArprintGuideBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityArprintGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppDataPref.isArGuideFirst = false
        AppDataPref.save(this)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        setGuideImagePager()

        binding.printStartView.setOnClickListener {
            //개발중 임시 진입
            // 용지와 프린트 모두 연결되있을때 영상 선택화면
            val intent = Intent(this, VideoSelectActivity::class.java)
            intent.putExtra(
                "FROM_HOME_CATEGORY",
                getString(R.string.cartridge_empty_action_text_1)
            )
            startActivity(intent)

//            // 처음 진입은 아니지만 용지가 없거나 프린트 미연결상태 용지화면으로 이동
//            val intent = Intent(this, EmptyCartridgeActivity::class.java)
//            intent.putExtra(
//                "FROM_HOME_CATEGORY",
//                getString(R.string.cartridge_empty_action_text_1)
//            )
//            startActivity(intent)

            finish()
        }
    }

    private fun setGuideImagePager() {
        //임시 data
        val arrayList = ArrayList<Int>()
        arrayList.add(1)
        arrayList.add(1)
        arrayList.add(1)

        binding.pagerView.adapter = ArGuidePagerAdapter(
            this,
            binding.pagerView,
            arrayList,
            object : ArGuidePagerAdapter.OnPageChangeListener {
                override fun onPageChanged(currentPage: Int) {
                    if (currentPage != 2) {
                        binding.startView.visibility = View.GONE
                    } else {
                        binding.startView.visibility = View.VISIBLE
                    }
                }
            })
        binding.pagerView.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.dotIndicator.attachTo(binding.pagerView)
    }
}