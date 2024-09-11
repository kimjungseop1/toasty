package com.syncrown.toasty.ui.component.home.tab1_home.ar_print.edit

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.ActivityEditVideoPrintBinding
import com.syncrown.toasty.databinding.CustomDrawingViewBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.commons.ActivityFinishManager
import com.syncrown.toasty.ui.commons.CommonFunc
import com.syncrown.toasty.ui.commons.DialogCommon
import com.syncrown.toasty.ui.commons.HorizontalSpaceItemDecoration
import com.syncrown.toasty.ui.component.home.MainActivity
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.edit.adapter.NavBarItem
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.edit.adapter.ThumbnailAdapter
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.edit.adapter.VideoEditAdapter
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.videotrimmer.TrimVideoActivity
import com.syncrown.toasty.ui.editor.CustomDrawingView
import com.syncrown.toasty.ui.editor.RotatableDraggableEditText


class EditVideoPrintActivity : BaseActivity() {
    private lateinit var binding: ActivityEditVideoPrintBinding
    private val editVidePrintViewModel: EditVidePrintViewModel by viewModels()

    private lateinit var dialogCommon: DialogCommon

    private var videoPath: String = ""

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            //소프트키 뒤로 버튼 처리
            dialogCommon.showEditCancel(supportFragmentManager, {
                //TODO 계속편집
            }, {
                //TODO 편집 취소시 캐시 삭제 -> 메인
                CommonFunc.clearAppCache(this@EditVideoPrintActivity)
                goMain()
            })

        }
    }

    // 생성된 DrawingView가 있는지 확인하는 변수
    private var isDrawingViewCreated = false
    private var drawingView: CustomDrawingView? = null


    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityEditVideoPrintBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(callback)

        dialogCommon = DialogCommon()

        binding.actionbar.actionBack.setOnClickListener {
            dialogCommon.showEditCancel(supportFragmentManager, {
                //TODO 계속편집
            }, {
                //TODO 편집 취소시 캐시 삭제 -> 메인
                CommonFunc.clearAppCache(this)
                goMain()
            })
        }
        binding.actionbar.actionTitle.text =
            ContextCompat.getString(this, R.string.edit_video_print_title)
        binding.actionbar.actionEtc.text =
            ContextCompat.getString(this, R.string.edit_video_print_print)
        binding.actionbar.actionEtc.setOnClickListener {
            //TODO 인쇄
        }

        videoPath = intent.getStringExtra("CACHE_FILE_PATH").toString()
        Log.e("jung", "-- receive : $videoPath")

        // 진입시 썸네일의 첫번째 이미지 바로 표시
        Glide.with(this)
            .load(getThumbnails(videoPath)[0])
            .into(binding.photoEditorImageView)

        setBottomMenuList()
    }

    private fun setBottomMenuList() {
        val arrayList = ArrayList<NavBarItem>()
        arrayList.add(NavBarItem(R.drawable.icon_change_video, R.drawable.icon_sel_change, "영상 변경"))
        arrayList.add(NavBarItem(R.drawable.icon_video_thumbnail, R.drawable.icon_sel_thumbnail, "썸네일"))
        arrayList.add(NavBarItem(R.drawable.icon_video_frame, R.drawable.icon_sel_frame, "영역"))
        arrayList.add(NavBarItem(R.drawable.icon_video_text, R.drawable.icon_sel_text, "텍스트"))
        arrayList.add(NavBarItem(R.drawable.icon_video_draw, R.drawable.icon_sel_draw, "드로잉"))
        arrayList.add(NavBarItem(R.drawable.icon_video_smail, R.drawable.icon_sel_smile, "스티커"))
        arrayList.add(NavBarItem(R.drawable.icon_video_etc, R.drawable.icon_sel_etc, "기타"))

        binding.recyclerEdit.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerEdit.adapter = VideoEditAdapter(arrayList) { position: Int ->

            when (position) {
                0 -> {
                    //영상변경 1.캐시에 저장된 비디오 제거, 현재페이지 종료, 이전페이지 종료
                    CommonFunc.clearAppCache(this)
                    finish()
                    ActivityFinishManager.finishActivityEvent.postValue(TrimVideoActivity::class.java)
                }

                1 -> {
                    //썸네일
                    if (binding.thumbLayout.root.visibility == View.VISIBLE) {
                        binding.thumbLayout.root.visibility = View.GONE
                    } else {
                        binding.thumbLayout.root.visibility = View.VISIBLE
                    }

                    val thumbnails = getThumbnails(videoPath)

                    binding.thumbLayout.thumbRecycler.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    binding.thumbLayout.thumbRecycler.adapter =
                        ThumbnailAdapter(this, thumbnails) { position: Int ->
                            Glide.with(this).load(thumbnails[position]).into(binding.photoEditorImageView)
                        }
                }

                2 -> {
                    // 영역

                }

                3 -> {
                    if (binding.textLayout.root.visibility == View.VISIBLE) {
                        binding.textLayout.root.visibility = View.GONE
                    } else {
                        binding.textLayout.root.visibility = View.VISIBLE
                        // 텍스트 누를때마다 영역 추가
                        addDraggableEditText()
                    }

                    binding.textLayout.itemColor.setOnClickListener {
                        binding.textLayout.itemColor.isActivated = true
                        binding.textLayout.itemStyle.isActivated = false
                        binding.textLayout.itemFont.isActivated = false

                        binding.textLayout.colorView.visibility = View.VISIBLE
                    }

                    binding.textLayout.itemStyle.setOnClickListener {
                        binding.textLayout.itemColor.isActivated = false
                        binding.textLayout.itemStyle.isActivated = true
                        binding.textLayout.itemFont.isActivated = false

                        binding.textLayout.styleView.visibility = View.VISIBLE
                    }

                    binding.textLayout.itemFont.setOnClickListener {
                        binding.textLayout.itemColor.isActivated = false
                        binding.textLayout.itemStyle.isActivated = false
                        binding.textLayout.itemFont.isActivated = true

                        binding.textLayout.fontView.visibility = View.VISIBLE
                    }

                    //TODO 컬러 선택 영역 처리

                    //TODO 텍스트 속성 처리

                    //TODO 폰트 선택 처리

                }

                4 -> {
                    // 드로잉
                    if (isDrawingViewCreated) {
                        // 이미 DrawingView가 생성된 상태에서 다시 누르면 로그 출력
                        Log.d(TAG, "DrawingView is already created.")
                        drawingView?.finalizeDrawing()
//                        drawingView?.finalizeDrag(true)
                        isDrawingViewCreated = false
                    } else {
                        // DrawingView가 생성되지 않은 경우 생성
                        val inflater = LayoutInflater.from(this)
                        val drawingViewBinding = CustomDrawingViewBinding.inflate(inflater, binding.container, true)

                        drawingViewBinding.drawingView.attachViews(
                            drawingViewBinding.rotateButton,
                            drawingViewBinding.deleteButton,
                            drawingViewBinding.borderView
                        )

                        drawingView = drawingViewBinding.drawingView
//                        drawingView?.finalizeDrag(false)

                        // DrawingView가 생성되었음을 플래그로 표시
                        isDrawingViewCreated = true
                    }

                }

                5 -> {
                    //스티커
                }

                6 -> {
                    //기타
                    if (binding.etcLayout.root.visibility == View.VISIBLE) {
                        binding.etcLayout.root.visibility = View.GONE
                    } else {
                        binding.etcLayout.root.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding.recyclerEdit.addItemDecoration(
            HorizontalSpaceItemDecoration(
                CommonFunc.dpToPx(
                    10,
                    this
                )
            )
        )

    }

    private fun addDraggableEditText() {
        val editText = RotatableDraggableEditText(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            // 고유한 ID를 생성하여 할당
            id = View.generateViewId()

            this.getEditText().setOnEditorActionListener { _, actionId, _ ->
                Log.e("jung", "actionId: $actionId")

                if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                    // 키보드 숨기기
                    val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(this.getEditText().windowToken, 0)
                    // 추가 동작
                    binding.recyclerEdit.findViewHolderForLayoutPosition(3)?.itemView?.performClick()

                    this.getEditText().clearFocus()
                    true
                } else {
                    false
                }
            }

            this.getEditText().setOnFocusChangeListener { view, hasFocus ->
                Log.e("jung","view id : " + view.id + ", id : " + id)
                if (hasFocus) {
                    binding.textLayout.root.visibility = View.VISIBLE
                } else {
                    binding.textLayout.root.visibility = View.GONE
                }
                updateBackground(hasFocus)
            }
        }

        binding.container.addView(editText)
    }

    // 썸네일 추출 함수
    private fun getThumbnails(videoPath: String): List<Bitmap> {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(videoPath)

        val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val durationMillis = durationString?.toLong() ?: 0L
        val thumbnails = mutableListOf<Bitmap>()

        // 최대 1초마다 프레임을 가져오기 (0초, 1초, 2초, ...)
        for (i in 0 until durationMillis step 1000) {
            val frameTimeUs = i * 1000L
            retriever.getFrameAtTime(frameTimeUs, MediaMetadataRetriever.OPTION_CLOSEST)?.let { frame ->
                thumbnails.add(frame)
            }
        }

        retriever.release()
        return thumbnails
    }


    private fun goMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}