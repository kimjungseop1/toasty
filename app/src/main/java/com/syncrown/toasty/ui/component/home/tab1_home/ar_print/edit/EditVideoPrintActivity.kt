package com.syncrown.toasty.ui.component.home.tab1_home.ar_print.edit

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.ActivityEditVideoPrintBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.ActivityFinishManager
import com.syncrown.toasty.ui.commons.CommonFunc
import com.syncrown.toasty.ui.commons.DialogCommon
import com.syncrown.toasty.ui.commons.HorizontalSpaceItemDecoration
import com.syncrown.toasty.ui.component.home.MainActivity
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.edit.adapter.ColorPickerAdapter
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.edit.adapter.NavBarItem
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.edit.adapter.StickerAdapter
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.edit.adapter.ThumbnailAdapter
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.edit.adapter.VideoEditAdapter
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.videotrimmer.TrimVideoActivity
import com.syncrown.toasty.ui.photoeditor.OnPhotoEditorListener
import com.syncrown.toasty.ui.photoeditor.PhotoEditor
import com.syncrown.toasty.ui.photoeditor.PhotoEditorView
import com.syncrown.toasty.ui.photoeditor.TextStyleBuilder
import com.syncrown.toasty.ui.photoeditor.ViewType
import com.syncrown.toasty.ui.photoeditor.shape.ShapeBuilder


class EditVideoPrintActivity : BaseActivity(), OnPhotoEditorListener,
    StickerAdapter.StickerListener {
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

    private lateinit var mPhotoEditor: PhotoEditor
    private lateinit var mPhotoEditorView: PhotoEditorView
    private lateinit var mShapeBuilder: ShapeBuilder
    private var mColorCode = 0

//    // 생성된 DrawingView가 있는지 확인하는 변수
//    private var isDrawingViewCreated = false
//    private var drawingView: CustomDrawingView? = null

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
        // 진입시 썸네일의 첫번째 이미지 바로 표시
        val thumbnails = getThumbnails(videoPath)
        if (thumbnails.isNotEmpty()) {
            binding.photoEditorImageView.source.setImageBitmap(thumbnails[0])
        }


        /** 에디터 **/
        mPhotoEditorView = binding.photoEditorImageView
        mPhotoEditor = PhotoEditor.Builder(this, mPhotoEditorView)
            .setPinchTextScalable(true)
            //.setDefaultTextTypeface(mTextRobotoTf)
            //.setDefaultEmojiTypeface(mEmojiTypeFace)
            .build()

        mPhotoEditor.setOnPhotoEditorListener(this)

        setBottomMenuList()
    }

    private fun setBottomMenuList() {
        val arrayList = ArrayList<NavBarItem>()
        arrayList.add(NavBarItem(R.drawable.icon_change_video, R.drawable.icon_sel_change, "영상 변경"))
        arrayList.add(
            NavBarItem(
                R.drawable.icon_video_thumbnail,
                R.drawable.icon_sel_thumbnail,
                "썸네일"
            )
        )
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
                    //TODO 영상변경 1.캐시에 저장된 비디오 제거, 현재페이지 종료, 이전페이지 종료
                    CommonFunc.clearAppCache(this)
                    finish()
                    ActivityFinishManager.finishActivityEvent.postValue(TrimVideoActivity::class.java)
                }

                1 -> {
                    //TODO 썸네일
                    showBottomStateVisible(position)

                    val thumbnails = getThumbnails(videoPath)

                    binding.thumbLayout.thumbRecycler.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    binding.thumbLayout.thumbRecycler.adapter =
                        ThumbnailAdapter(this, thumbnails) { position: Int ->
                            binding.photoEditorImageView.source.setImageBitmap(thumbnails[position])
                        }
                    binding.thumbLayout.thumbRecycler.setHasFixedSize(true)
                }

                2 -> {
                    //TODO  영역
                    Log.e(TAG, "crop")
                }

                3 -> {
                    //TODO  텍스트
                    showBottomStateVisible(position)

                    val layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    binding.textLayout.addTextColorPickerRecyclerView.layoutManager =
                        layoutManager
                    binding.textLayout.addTextColorPickerRecyclerView.setHasFixedSize(true)
                    val colorPickerAdapter = ColorPickerAdapter(this)

                    colorPickerAdapter.setOnColorPickerClickListener(object :
                        ColorPickerAdapter.OnColorPickerClickListener {
                        override fun onColorPickerClickListener(colorCode: Int) {
                            mColorCode = colorCode
                            binding.textLayout.addTextEditText.setTextColor(colorCode)
                        }
                    })

                    binding.textLayout.addTextColorPickerRecyclerView.adapter =
                        colorPickerAdapter

                    binding.textLayout.addTextDoneTv.setOnClickListener {
                        val styleBuilder = TextStyleBuilder()
                        styleBuilder.withTextColor(mColorCode)

                        val editString = binding.textLayout.addTextEditText.text.toString()

                        mPhotoEditor.addText(editString, styleBuilder)

                        //초기화
                        binding.textLayout.addTextEditText.text = null
                        binding.textLayout.root.visibility = View.GONE
                    }

                }

                4 -> {
                    //TODO  드로잉
                    showBottomStateVisible(position)

                    if (binding.drawingLayout.root.visibility == View.VISIBLE) {
                        mPhotoEditor.setBrushDrawingMode(true)
                        mShapeBuilder = ShapeBuilder()
                        mPhotoEditor.setShape(mShapeBuilder)

                        val layoutManager =
                            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                        binding.drawingLayout.shapeColors.layoutManager = layoutManager
                        binding.drawingLayout.shapeColors.setHasFixedSize(true)
                        val colorPickerAdapter = ColorPickerAdapter(this)
                        colorPickerAdapter.setOnColorPickerClickListener(object :
                            ColorPickerAdapter.OnColorPickerClickListener {
                            override fun onColorPickerClickListener(colorCode: Int) {
                                mPhotoEditor.setShape(mShapeBuilder.withShapeColor(colorCode))
                            }
                        })
                        binding.drawingLayout.shapeColors.adapter = colorPickerAdapter

                        binding.drawingLayout.undoBtn.setOnClickListener {
                           mPhotoEditor.undo()
                        }

                        binding.drawingLayout.redoBtn.setOnClickListener {
                            mPhotoEditor.redo()
                        }

                    } else {
                        mPhotoEditor.setBrushDrawingMode(false)
                    }


//                    if (isDrawingViewCreated) {
//                        // 이미 DrawingView가 생성된 상태에서 다시 누르면 로그 출력
//                        Log.d(TAG, "DrawingView is already created.")
//                        drawingView?.finalizeDrawing()
//                        isDrawingViewCreated = false
//                    } else {
//                        // DrawingView가 생성되지 않은 경우 생성
//                        val inflater = LayoutInflater.from(this)
//                        val drawingViewBinding = CustomDrawingViewBinding.inflate(inflater, binding.container, true)
//
//                        drawingViewBinding.drawingView.attachViews(
//                            drawingViewBinding.rotateButton,
//                            drawingViewBinding.deleteButton,
//                            drawingViewBinding.borderView
//                        )
//
//                        drawingView = drawingViewBinding.drawingView
//                        // DrawingView가 생성되었음을 플래그로 표시
//                        isDrawingViewCreated = true
//                    }

                }

                5 -> {
                    //TODO 스티커
                    showBottomStateVisible(position)

                    val gridLayoutManager = GridLayoutManager(this, 3)
                    binding.stickerLayout.rvEmoji.layoutManager = gridLayoutManager
                    val stickerAdapter = StickerAdapter(this)
                    binding.stickerLayout.rvEmoji.adapter = stickerAdapter
                    binding.stickerLayout.rvEmoji.setHasFixedSize(true)
                    stickerAdapter.setStickerListener(this)

                }

                6 -> {
                    //TODO 기타

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
            retriever.getFrameAtTime(frameTimeUs, MediaMetadataRetriever.OPTION_CLOSEST)
                ?.let { frame ->
                    thumbnails.add(frame)
                }
        }

        retriever.release()
        return thumbnails
    }

    private fun showBottomStateVisible(position: Int) {
        when (position) {
            0 -> {
                // 영상변경
            }

            1 -> {
                // 썸네일
                if (binding.thumbLayout.root.visibility == View.VISIBLE) {
                    binding.thumbLayout.root.visibility = View.GONE
                } else {
                    binding.thumbLayout.root.visibility = View.VISIBLE
                }
                binding.textLayout.root.visibility = View.GONE
                binding.drawingLayout.root.visibility = View.GONE
                binding.stickerLayout.root.visibility = View.GONE
                binding.etcLayout.root.visibility = View.GONE
            }

            2 -> {
                // 영역
            }

            3 -> {
                // 텍스트
                if (binding.textLayout.root.visibility == View.VISIBLE) {
                    binding.textLayout.root.visibility = View.GONE
                } else {
                    binding.textLayout.root.visibility = View.VISIBLE
                }
                binding.thumbLayout.root.visibility = View.GONE
                binding.drawingLayout.root.visibility = View.GONE
                binding.stickerLayout.root.visibility = View.GONE
                binding.etcLayout.root.visibility = View.GONE
            }

            4 -> {
                // 드로잉
                if (binding.drawingLayout.root.visibility == View.VISIBLE) {
                    binding.drawingLayout.root.visibility = View.GONE
                } else {
                    binding.drawingLayout.root.visibility = View.VISIBLE
                }
                binding.thumbLayout.root.visibility = View.GONE
                binding.textLayout.root.visibility = View.GONE
                binding.stickerLayout.root.visibility = View.GONE
                binding.etcLayout.root.visibility = View.GONE
            }

            5 -> {
                //스티커
                if (binding.stickerLayout.root.visibility == View.VISIBLE) {
                    binding.stickerLayout.root.visibility = View.GONE
                } else {
                    binding.stickerLayout.root.visibility = View.VISIBLE
                }
                binding.thumbLayout.root.visibility = View.GONE
                binding.textLayout.root.visibility = View.GONE
                binding.drawingLayout.root.visibility = View.GONE
                binding.etcLayout.root.visibility = View.GONE
            }

            6 -> {
                //기타
                if (binding.etcLayout.root.visibility == View.VISIBLE) {
                    binding.etcLayout.root.visibility = View.GONE
                } else {
                    binding.etcLayout.root.visibility = View.VISIBLE
                }
                binding.thumbLayout.root.visibility = View.GONE
                binding.textLayout.root.visibility = View.GONE
                binding.drawingLayout.root.visibility = View.GONE
                binding.stickerLayout.root.visibility = View.GONE
            }
        }
    }

    /** 에디터 관련 리스너 start----- **/
    override fun onEditTextChangeListener(rootView: View, text: String, colorCode: Int) {

    }

    override fun onAddViewListener(viewType: ViewType, numberOfAddedViews: Int) {

    }

    override fun onRemoveViewListener(viewType: ViewType, numberOfAddedViews: Int) {

    }
    /** 에디터 관련 리스너 end----- **/

    /** 드로잉 관련 리스너 start----- **/
    override fun onStartViewChangeListener(viewType: ViewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [$viewType]")
    }

    override fun onStopViewChangeListener(viewType: ViewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [$viewType]")
    }

    override fun onTouchSourceImage(event: MotionEvent) {
        Log.d(TAG, "onTouchView() called with: event = [$event]")
    }
    /** ----- 드로잉 관련 리스너 end **/

    /** 스티커 관련 리스너 start------ **/
    override fun onStickerClick(bitmap: Bitmap) {
        mPhotoEditor.addImage(bitmap)
        binding.recyclerEdit.findViewHolderForLayoutPosition(5)?.itemView?.performClick()

    }

    /** ----- 스티커 관련 리스너 end **/

    private fun goMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}