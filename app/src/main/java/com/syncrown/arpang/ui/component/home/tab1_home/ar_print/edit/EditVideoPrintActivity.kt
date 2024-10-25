package com.syncrown.arpang.ui.component.home.tab1_home.ar_print.edit

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityEditVideoPrintBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.commons.FontManager
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.commons.HorizontalSpaceItemDecoration
import com.syncrown.arpang.ui.component.home.MainActivity
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.ActivityFinishManager
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.ArImageStorage
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.edit.adapter.NavBarItem
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.edit.adapter.StickerAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.edit.adapter.ThumbnailAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.edit.adapter.VideoEditAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.preview.ArPrintPreviewActivity
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.tag.ArPrintTagSettingActivity
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videotrimmer.TrimVideoActivity
import com.syncrown.arpang.ui.component.home.tab1_home.label_sticker.adapter.FontAdapter
import com.syncrown.arpang.ui.photoeditor.OnPhotoEditorListener
import com.syncrown.arpang.ui.photoeditor.PhotoEditor
import com.syncrown.arpang.ui.photoeditor.PhotoEditorView
import com.syncrown.arpang.ui.photoeditor.TextStyleBuilder
import com.syncrown.arpang.ui.photoeditor.ViewType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.angmarch.views.OnSpinnerItemSelectedListener
import java.util.LinkedList


class EditVideoPrintActivity : BaseActivity(), OnPhotoEditorListener,
    StickerAdapter.StickerListener {
    private lateinit var binding: ActivityEditVideoPrintBinding
    private val editVidePrintViewModel: EditVidePrintViewModel by viewModels()

    private lateinit var dialogCommon: DialogCommon

    private var videoPath: String = ""

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            dialogCommon.showEditCancel(supportFragmentManager, {
                //TODO 계속편집
            }, {
                //TODO 편집 취소
                finish()
            })
        }
    }

    private lateinit var mPhotoEditor: PhotoEditor

    private lateinit var thumbnails: List<Bitmap>

    private lateinit var resultBitmap: Bitmap

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
                //TODO 편집 취소
                finish()
            })
        }
        binding.actionbar.actionTitle.text =
            ContextCompat.getString(this, R.string.edit_video_print_title)
        binding.actionbar.actionEtc.text =
            ContextCompat.getString(this, R.string.edit_video_print_print)
        binding.actionbar.actionEtc.setOnClickListener {
            //TODO 인쇄
            if (AppDataPref.isArPrintPreView) {
                goPreview()
            } else {
                resultBitmap = CommonFunc.getBitmapFromView(binding.printAreaView)
                ArImageStorage.bitmap = resultBitmap
            }
        }

        videoPath = intent.getStringExtra("CACHE_FILE_PATH").toString()
        // 진입시 썸네일의 첫번째 이미지 바로 표시
        lifecycleScope.launch {
            binding.photoEditorImageView.source.setImageBitmap(getFirstThumbnail(videoPath))
            withContext(Dispatchers.IO) {
                thumbnails = getThumbnails(videoPath)
            }
        }

        /** 에디터 초기화 **/
        mPhotoEditor = PhotoEditor.Builder(this, binding.photoEditorImageView)
            .setPinchTextScalable(true)
            .build()
        mPhotoEditor.setOnPhotoEditorListener(this)

        setBottomMenuList()
    }

    private fun setBottomMenuList() {
        val arrayList = ArrayList<NavBarItem>()
        arrayList.add(
            NavBarItem(
                R.drawable.icon_change_video,
                R.drawable.icon_sel_change,
                getString(R.string.editor_video_menu_1)
            )
        )
        arrayList.add(
            NavBarItem(
                R.drawable.icon_video_thumbnail,
                R.drawable.icon_sel_thumbnail,
                getString(R.string.editor_video_menu_2)
            )
        )
        arrayList.add(
            NavBarItem(
                R.drawable.icon_video_frame,
                R.drawable.icon_sel_frame,
                getString(R.string.editor_video_menu_3)
            )
        )
        arrayList.add(
            NavBarItem(
                R.drawable.icon_video_text,
                R.drawable.icon_sel_text,
                getString(R.string.editor_video_menu_4)
            )
        )
        arrayList.add(
            NavBarItem(
                R.drawable.icon_video_draw,
                R.drawable.icon_sel_draw,
                getString(R.string.editor_video_menu_5)
            )
        )
        arrayList.add(
            NavBarItem(
                R.drawable.icon_video_smail,
                R.drawable.icon_sel_smile,
                getString(R.string.editor_video_menu_6)
            )
        )
        arrayList.add(
            NavBarItem(
                R.drawable.icon_video_etc,
                R.drawable.icon_sel_etc,
                getString(R.string.editor_video_menu_7)
            )
        )

        binding.recyclerEdit.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerEdit.adapter = VideoEditAdapter(arrayList) { position: Int ->
            binding.thumbLayout.root.visibility = View.GONE
            binding.textEditView.root.visibility = View.GONE
            binding.iconView.root.visibility = View.GONE
            binding.etcSettingView.root.visibility = View.GONE
            mPhotoEditor.clearHelperBox()
            mPhotoEditor.setBrushDrawingMode(false)
            hideKeyBoard()

            when (position) {
                0 -> {
                    //TODO 영상변경 1.캐시에 저장된 비디오 제거, 현재페이지 종료, 이전페이지 종료
                    CommonFunc.clearAppCache(this)
                    finish()
                    ActivityFinishManager.finishActivityEvent.postValue(TrimVideoActivity::class.java)
                }

                1 -> {
                    //TODO 썸네일
                    if (binding.thumbLayout.root.visibility == View.VISIBLE) {
                        binding.thumbLayout.root.visibility = View.GONE
                    } else {
                        binding.thumbLayout.root.visibility = View.VISIBLE
                    }

                    binding.thumbLayout.thumbRecycler.layoutManager =
                        LinearLayoutManager(
                            this@EditVideoPrintActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )

                    if (::thumbnails.isInitialized) {
                        binding.thumbLayout.thumbRecycler.adapter =
                            ThumbnailAdapter(
                                this@EditVideoPrintActivity,
                                thumbnails
                            ) { position: Int ->
                                binding.photoEditorImageView.source.setImageBitmap(thumbnails[position])
                            }
                        binding.thumbLayout.thumbRecycler.setHasFixedSize(true)
                    } else {
                        // Handler를 사용해 주기적으로 초기화 상태를 확인
                        val handler = Handler(Looper.getMainLooper())
                        val checkInitialization = object : Runnable {
                            override fun run() {
                                if (::thumbnails.isInitialized) {
                                    binding.thumbLayout.thumbRecycler.adapter =
                                        ThumbnailAdapter(
                                            this@EditVideoPrintActivity,
                                            thumbnails
                                        ) { position: Int ->
                                            binding.photoEditorImageView.source.setImageBitmap(thumbnails[position])
                                        }
                                    binding.thumbLayout.thumbRecycler.setHasFixedSize(true)
                                } else {
                                    handler.postDelayed(this, 500)
                                }
                            }
                        }
                        handler.post(checkInitialization)
                    }

                }

                2 -> {
                    //TODO  영역
                    Toast.makeText(this, "영역...", Toast.LENGTH_SHORT).show()
                }

                3 -> {
                    //TODO  텍스트
                    if (binding.textEditView.root.visibility == View.VISIBLE) {
                        binding.textEditView.root.visibility = View.GONE
                    } else {
                        binding.textEditView.root.visibility = View.VISIBLE
                    }

                    val styleBuilder = TextStyleBuilder()
                    styleBuilder.withTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.color_white
                        )
                    )

                    mPhotoEditor.addEditText(styleBuilder)

                    setEditFont()
                    setEditText()
                }

                4 -> {
                    //TODO  드로잉
                    mPhotoEditor.addDrawing()
                }

                5 -> {
                    //TODO 스티커
                    if (binding.iconView.root.visibility == View.VISIBLE) {
                        binding.iconView.root.visibility = View.GONE
                    } else {
                        binding.iconView.root.visibility = View.VISIBLE
                    }

                    setStickerIcon()
                }

                6 -> {
                    //TODO 기타
                    if (binding.etcSettingView.root.visibility == View.VISIBLE) {
                        binding.etcSettingView.root.visibility = View.GONE
                    } else {
                        binding.etcSettingView.root.visibility = View.VISIBLE
                    }

                    setEtcSetting()
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

    private fun setEtcSetting() {
        binding.etcSettingView.publicSwitch.setOnCheckedChangeListener { _, isChecked ->

        }

        binding.etcSettingView.tagSetting.setOnClickListener {
            goTagSetting()
        }

        binding.etcSettingView.preview.setOnClickListener {
            goPreview()
        }
    }

    private fun setStickerIcon() {
        clearItemDecorations(binding.iconView.recyclerIcon)

        val gridLayoutManager = GridLayoutManager(this, 4)
        binding.iconView.recyclerIcon.layoutManager = gridLayoutManager
        binding.iconView.recyclerIcon.addItemDecoration(
            GridSpacingItemDecoration(
                4,
                CommonFunc.dpToPx(12, this),
                false
            )
        )

        val freeAdapter = StickerAdapter(this)
        binding.iconView.recyclerIcon.adapter = freeAdapter
        binding.iconView.recyclerIcon.setHasFixedSize(true)
        freeAdapter.setStickerListener(this)
    }

    private fun clearItemDecorations(recyclerView: RecyclerView) {
        val decorationCount = recyclerView.itemDecorationCount
        for (i in decorationCount - 1 downTo 0) {
            recyclerView.removeItemDecorationAt(i)
        }
    }

    private fun setEditFont() {
        binding.textEditView.editFont.setOnClickListener {
            binding.textEditView.editText.isSelected = false
            binding.textEditView.editFont.isSelected = true

            binding.textEditView.editView.visibility = View.GONE
            binding.textEditView.fontEditView.visibility = View.VISIBLE

            hideKeyBoard()
        }

        binding.textEditView.recyclerFont.layoutManager = LinearLayoutManager(this)
        val fontItems = FontManager.getFontItems()
        val adapter = FontAdapter(
            this,
            fontItems
        ) { _, typeface ->
            val focusedEditText = currentFocus as? AppCompatEditText

            focusedEditText?.typeface = typeface
        }
        binding.textEditView.recyclerFont.adapter = adapter

    }

    private fun setEditText() {
        binding.textEditView.editText.setOnClickListener {
            binding.textEditView.editText.isSelected = true
            binding.textEditView.editFont.isSelected = false

            binding.textEditView.editView.visibility = View.VISIBLE
            binding.textEditView.fontEditView.visibility = View.GONE

            hideKeyBoard()
        }

        val items: List<String> =
            LinkedList(mutableListOf("10", "12", "14", "16", "18", "20", "24"))
        binding.textEditView.sizeSpinner.attachDataSource(items)
        binding.textEditView.sizeSpinner.selectedIndex = 6
        binding.textEditView.sizeSpinner.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.color_white
            )
        )
        binding.textEditView.sizeSpinner.onSpinnerItemSelectedListener =
            OnSpinnerItemSelectedListener { _, _, position, _ ->
                binding.textEditView.sizeSpinner.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_white
                    )
                )

                val focusedEditText = currentFocus as? AppCompatEditText
                val textSize = when (position) {
                    0 -> 10f
                    1 -> 12f
                    2 -> 14f
                    3 -> 16f
                    4 -> 18f
                    5 -> 20f
                    6 -> 24f
                    else -> 24f
                }

                focusedEditText?.textSize = textSize
            }

        binding.textEditView.groupVertical.visibility = View.GONE

        binding.textEditView.alignLeftBtn.setOnClickListener {
            val focusedEditText = currentFocus as? AppCompatEditText
            binding.textEditView.alignLeftBtn.isSelected = true
            binding.textEditView.alignCenterBtn.isSelected = false
            binding.textEditView.alignRightBtn.isSelected = false

            focusedEditText?.gravity = Gravity.LEFT
        }

        binding.textEditView.alignCenterBtn.setOnClickListener {
            val focusedEditText = currentFocus as? AppCompatEditText
            binding.textEditView.alignLeftBtn.isSelected = false
            binding.textEditView.alignCenterBtn.isSelected = true
            binding.textEditView.alignRightBtn.isSelected = false

            focusedEditText?.gravity = Gravity.CENTER
        }

        binding.textEditView.alignRightBtn.setOnClickListener {
            val focusedEditText = currentFocus as? AppCompatEditText
            binding.textEditView.alignLeftBtn.isSelected = false
            binding.textEditView.alignCenterBtn.isSelected = false
            binding.textEditView.alignRightBtn.isSelected = true

            focusedEditText?.gravity = Gravity.RIGHT
        }

        binding.textEditView.thinBtn.setOnClickListener {
            val focusedEditText = currentFocus as? AppCompatEditText
            binding.textEditView.thinBtn.isSelected = true
            binding.textEditView.boldBtn.isSelected = false

            focusedEditText?.typeface = Typeface.DEFAULT
        }

        binding.textEditView.boldBtn.setOnClickListener {
            val focusedEditText = currentFocus as? AppCompatEditText
            binding.textEditView.thinBtn.isSelected = false
            binding.textEditView.boldBtn.isSelected = true

            focusedEditText?.typeface = Typeface.DEFAULT_BOLD
        }

        binding.textEditView.styleBtn.isSelected = false
        binding.textEditView.styleBtn.setOnClickListener {
            val focusedEditText = currentFocus as? AppCompatEditText
            if (binding.textEditView.styleBtn.isSelected) {
                binding.textEditView.styleBtn.isSelected = false
                focusedEditText?.paintFlags = Paint.LINEAR_TEXT_FLAG
            } else {
                binding.textEditView.styleBtn.isSelected = true
                focusedEditText?.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            }
        }
    }

    private fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let { view ->
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
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
            Log.e("jung", "load start : $i")
            val frameTimeUs = i * 1000L
            retriever.getFrameAtTime(frameTimeUs, MediaMetadataRetriever.OPTION_CLOSEST)
                ?.let { frame ->
                    thumbnails.add(frame)
                }
        }

        Log.e("jung", "load end")
        retriever.release()
        return thumbnails
    }

    private fun getFirstThumbnail(videoPath: String): Bitmap? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(videoPath)

        // 0초 시점의 프레임을 가져오기
        val firstFrame = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST)

        retriever.release()
        return firstFrame
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

    private fun goPreview() {
        resultBitmap = CommonFunc.getBitmapFromView(binding.printAreaView)
        ArImageStorage.bitmap = resultBitmap

        val intent = Intent(this, ArPrintPreviewActivity::class.java)
        startActivity(intent)
    }

    private fun goTagSetting() {
        val intent = Intent(this, ArPrintTagSettingActivity::class.java)
        startActivity(intent)
    }

    private fun goMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}