package com.syncrown.arpang.ui.component.home.tab1_home.ar_print.edit

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
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
import com.syncrown.arpang.db.ar_db.ArVideoImageDatabase
import com.syncrown.arpang.db.ar_db.ArVideoImageEntity
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.commons.FontManager
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.commons.HorizontalSpaceItemDecoration
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
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.crop.CropImageActivity
import com.syncrown.arpang.ui.photoeditor.OnPhotoEditorListener
import com.syncrown.arpang.ui.photoeditor.PhotoEditor
import com.syncrown.arpang.ui.photoeditor.TextStyleBuilder
import com.syncrown.arpang.ui.photoeditor.ViewType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.angmarch.views.OnSpinnerItemSelectedListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.LinkedList


class EditVideoPrintActivity : BaseActivity(), OnPhotoEditorListener,
    StickerAdapter.StickerListener {
    private lateinit var binding: ActivityEditVideoPrintBinding
    private val editVidePrintViewModel: EditVidePrintViewModel by viewModels()

    private lateinit var dialogCommon: DialogCommon

    private lateinit var videoPath: Uri

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
            mPhotoEditor.clearHelperBox()

            //TODO 인쇄
            if (AppDataPref.isArPrintPreView) {
                goPreview()
            } else {
                resultBitmap = CommonFunc.getBitmapFromView(binding.printAreaView)
                ArImageStorage.bitmap = resultBitmap

                //최종 만들어진 이미지 저장
                saveBitmapToExternalStorage()

                //최종 만들어진 비디오 저장
                saveVideo(videoPath.toString())

                saveRoomDB()


            }
        }

        val path = intent.getStringExtra("CACHE_FILE_PATH")
        val uri = Uri.parse(path)
        videoPath = uri
        Log.e("jung", "oncreate : $path, ${videoPath.lastPathSegment}")

        // 진입시 썸네일의 첫번째 이미지 바로 표시
        lifecycleScope.launch {
            binding.photoEditorImageView.source.setImageBitmap(
                getFirstThumbnail(
                    this@EditVideoPrintActivity,
                    videoPath
                )
            )
            withContext(Dispatchers.Main) {
                thumbnails = getThumbnails(this@EditVideoPrintActivity, videoPath)
            }
        }

        /** 에디터 초기화 **/
        mPhotoEditor = PhotoEditor.Builder(this, binding.photoEditorImageView)
            .setPinchTextScalable(true)
            .build()
        mPhotoEditor.setOnPhotoEditorListener(this)

        setBottomMenuList()
    }

    private fun saveRoomDB() {
        val database = ArVideoImageDatabase.getDatabase(this)
        val dao = database.arVideoImageDao()

        val entity = ArVideoImageEntity(
            videoPath = ArImageStorage.resultVideoPath,
            imagePath = ArImageStorage.resultImagePath
        )

        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    // 현재 데이터 개수 확인
                    val currentCount = dao.getRowCount()

                    // 데이터 개수가 1000개 이상이면 오래된 데이터 삭제
                    if (currentCount >= 1000) {
                        val excessCount = currentCount - 999 // 초과된 데이터 개수 계산
                        dao.deleteOldestEntries(excessCount)
                    }

                    // 새로운 데이터 삽입
                    dao.insertVideoImage(entity)
                }
                Log.d("jung", "비디오와 이미지 경로 DB 저장 성공")
            } catch (e: Exception) {
                Log.e("jung", "DB 저장 중 오류 발생: ${e.message}")
            }
        }

    }

    private fun saveVideo(path: String) {
        val result = moveVideoToExternalStorage(path)
        if (result.first) {
            Log.e("jung", "영상 복사 성공: ${result.second}")

            val cacheFile = File(path)
            if (cacheFile.exists()) {
                if (cacheFile.delete()) {
                    Log.e("jung", "캐시 파일 제거 성공")
                } else {
                    Log.e("jung", "캐시 파일 제거 실패")
                }
            }

        } else {
            Log.e("jung", "영상 복사 실패")
        }
    }

    private fun saveBitmapToExternalStorage(): Pair<Boolean, String?> {
        val bitmap = ArImageStorage.bitmap
        val videoFilePath = ArImageStorage.resultVideoPath

        // 비디오 파일 이름을 추출
        val videoFile = File(videoFilePath)
        val videoNameWithoutExtension = videoFile.nameWithoutExtension // 확장자를 제외한 파일명

        // 이미지 저장 경로 설정
        val externalMoviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val targetDir = File(externalMoviesDir, "ArPangVideo/Images")

        // 디렉터리 생성 확인
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            Log.e("jung", "디렉터리 생성 실패: ${targetDir.absolutePath}")
            return Pair(false, null)
        }

        // 저장할 이미지 파일 경로 설정
        val imageFile = File(targetDir, "$videoNameWithoutExtension.png")

        ArImageStorage.resultImagePath = imageFile.absolutePath

        return try {
            // 파일 출력 스트림 생성
            if (bitmap != null) {
                FileOutputStream(imageFile).use { fos ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                }
            }
            Log.d("jung", "이미지 저장 성공: ${imageFile.absolutePath}")
            Pair(true, imageFile.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("jung", "IOException 발생: ${e.message}")
            Pair(false, null)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("jung", "예상치 못한 예외 발생: ${e.message}")
            Pair(false, null)
        }
    }

    private fun moveVideoToExternalStorage(cacheFilePath: String): Pair<Boolean, String?> {
        val externalMoviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val targetDir = File(externalMoviesDir, "ArPangVideo/Videos")

        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }

        val cacheFile = File(cacheFilePath)
        val targetFile = File(targetDir, cacheFile.name)

        ArImageStorage.resultVideoPath = targetFile.absolutePath

        return try {
            cacheFile.copyTo(targetFile, overwrite = true)
            Pair(true, targetFile.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
            Pair(false, null)
        }
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

        var lastSelectedPosition = -1

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

            if (lastSelectedPosition == position) {
                lastSelectedPosition = -1
            } else {
                lastSelectedPosition = position

                when (position) {
                    0 -> {
                        //TODO 영상변경 1.캐시에 저장된 비디오 제거, 현재페이지 종료, 이전페이지 종료
                        CommonFunc.clearAppCache(this)
                        finish()
                        ActivityFinishManager.finishActivityEvent.postValue(TrimVideoActivity::class.java)
                    }

                    1 -> {
                        //TODO 썸네일
                        binding.thumbLayout.root.visibility = View.VISIBLE

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
                                                binding.photoEditorImageView.source.setImageBitmap(
                                                    thumbnails[position]
                                                )
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
                        if (::thumbnails.isInitialized) {
                            ArImageStorage.bitmap = thumbnails[position]

                            goCropImage(thumbnails[position])
                        }
                    }

                    3 -> {
                        //TODO  텍스트
                        binding.textEditView.root.visibility = View.VISIBLE

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
                        binding.iconView.root.visibility = View.VISIBLE
                        setStickerIcon()
                    }

                    6 -> {
                        //TODO 기타
                        binding.etcSettingView.root.visibility = View.VISIBLE

                        setEtcSetting()
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
    private fun getThumbnails(context: Context, videoUri: Uri): List<Bitmap> {
        val retriever = MediaMetadataRetriever()
        val thumbnails = mutableListOf<Bitmap>()

        try {
            retriever.setDataSource(context, videoUri)

            val durationString =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val durationMillis = durationString?.toLong() ?: 0L

            // 최대 1초마다 프레임을 가져오기 (0초, 1초, 2초, ...)
            for (i in 0 until durationMillis step 1000) {
                val frameTimeUs = i * 1000L
                retriever.getFrameAtTime(frameTimeUs, MediaMetadataRetriever.OPTION_CLOSEST)
                    ?.let { frame ->
                        thumbnails.add(frame)
                    }
            }
        } catch (e: Exception) {
            Log.e("jung", "Error retrieving thumbnails: ${e.message}")
            e.printStackTrace()
        } finally {
            retriever.release()
        }

        return thumbnails
    }

    private fun getFirstThumbnail(context: Context, videoUri: Uri): Bitmap? {
        val retriever = MediaMetadataRetriever()

        try {
            retriever.setDataSource(context, videoUri)
            // 0초 시점의 프레임을 가져오기
            val firstFrame = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST)
            retriever.release()
            return firstFrame
        } catch (e: Exception) {
            Log.e("jung", "Error retrieving first thumbnail: ${e.message}")
            e.printStackTrace()
            retriever.release()
            return null
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

    private fun goPreview() {
        mPhotoEditor.clearHelperBox()
        resultBitmap = CommonFunc.getBitmapFromView(binding.printAreaView)
        ArImageStorage.bitmap = resultBitmap

        val intent = Intent(this, ArPrintPreviewActivity::class.java)
        startActivity(intent)
    }

    private fun goTagSetting() {
        val intent = Intent(this, ArPrintTagSettingActivity::class.java)
        startActivity(intent)
    }

    private fun goCropImage(bitmap: Bitmap) {
        ArImageStorage.bitmap = bitmap

        val intent = Intent(this, CropImageActivity::class.java)
        cropImageLauncher.launch(intent)
    }

    private val cropImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val cropBitmap = ArImageStorage.bitmap

                binding.photoEditorImageView.source.setImageBitmap(cropBitmap)
            }
        }
}