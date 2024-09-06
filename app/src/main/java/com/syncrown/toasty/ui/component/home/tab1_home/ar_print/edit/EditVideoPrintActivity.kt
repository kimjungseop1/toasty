package com.syncrown.toasty.ui.component.home.tab1_home.ar_print.edit

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.ActivityEditVideoPrintBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.commons.CommonFunc
import com.syncrown.toasty.ui.commons.DialogCommon
import com.syncrown.toasty.ui.commons.HorizontalSpaceItemDecoration
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.edit.adapter.EditItem
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.edit.adapter.VideoEditAdapter

class EditVideoPrintActivity : BaseActivity() {
    private lateinit var binding: ActivityEditVideoPrintBinding
    private val editVidePrintViewModel: EditVidePrintViewModel by viewModels()

    private lateinit var dialogCommon: DialogCommon

    private var videoPath: String = ""

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityEditVideoPrintBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dialogCommon = DialogCommon()

        binding.actionbar.actionBack.setOnClickListener {
            dialogCommon.showEditCancel(supportFragmentManager, {
                //TODO nothing
            }, {
                //TODO 편집 취소시 캐시 삭제
                CommonFunc.clearAppCache(this)
                finish()
            })
        }
        binding.actionbar.actionTitle.text =
            ContextCompat.getString(this, R.string.edit_video_print_title)
        binding.actionbar.actionEtc.text =
            ContextCompat.getString(this, R.string.edit_video_print_print)
        binding.actionbar.actionEtc.setOnClickListener {
            //인쇄
        }

        videoPath = intent.getStringExtra("CACHE_FILE_PATH").toString()
        Log.e("jung", "-- receive : $videoPath")

        setBottomMenuList()
    }

    private fun setBottomMenuList() {
        val arrayList = ArrayList<EditItem>()
        arrayList.add(EditItem(R.drawable.icon_change_video, "영상 변경"))
        arrayList.add(EditItem(R.drawable.icon_video_thumbnail, "썸네일"))
        arrayList.add(EditItem(R.drawable.icon_video_frame, "영역"))
        arrayList.add(EditItem(R.drawable.icon_video_text, "텍스트"))
        arrayList.add(EditItem(R.drawable.icon_video_draw, "드로잉"))
        arrayList.add(EditItem(R.drawable.icon_video_smail, "스티커"))
        arrayList.add(EditItem(R.drawable.icon_video_etc, "기타"))

        binding.recyclerEdit.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerEdit.adapter = VideoEditAdapter(arrayList) { position: Int ->
            Log.e("jung", "click pos : $position")
            when(position) {
                0 -> {
                    //영상변경
                    CommonFunc.clearAppCache(this)
                }

                1 -> {
                    //썸네일
                }

                2 -> {
                    // 영역
                }

                3 -> {
                    // 텍스트
                }

                4 -> {
                    // 드로잉
                }

                5 -> {
                    //스티커
                }

                6 -> {
                    //기타
                }
            }
        }

        binding.recyclerEdit.addItemDecoration(HorizontalSpaceItemDecoration(CommonFunc.dpToPx(10, this)))

    }
}