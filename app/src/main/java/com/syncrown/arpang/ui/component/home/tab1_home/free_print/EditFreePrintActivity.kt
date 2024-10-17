package com.syncrown.arpang.ui.component.home.tab1_home.free_print

import FreeStickerAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityEditFreePrintBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.component.home.MainActivity
import com.syncrown.arpang.ui.component.home.tab1_home.free_print.preview.FreePrintPreviewActivity
import com.syncrown.arpang.ui.component.home.tab1_home.free_print.select.SelectFreePrintActivity
import com.syncrown.arpang.ui.component.home.tab1_home.free_print.setting.FreePrintTagSettingActivity
import com.syncrown.arpang.ui.photoeditor.OnPhotoEditorListener
import com.syncrown.arpang.ui.photoeditor.PhotoEditor
import com.syncrown.arpang.ui.photoeditor.PhotoEditorView
import com.syncrown.arpang.ui.photoeditor.TextStyleBuilder
import com.syncrown.arpang.ui.photoeditor.ViewType
import com.syncrown.arpang.ui.photoeditor.shape.ShapeBuilder
import com.yalantis.ucrop.UCrop
import org.angmarch.views.OnSpinnerItemSelectedListener
import java.util.LinkedList

class EditFreePrintActivity : BaseActivity(), FreeStickerAdapter.IconListener,
    OnPhotoEditorListener {
    private lateinit var binding: ActivityEditFreePrintBinding

    private lateinit var mPhotoEditor: PhotoEditor
    private lateinit var mPhotoEditorView: PhotoEditorView

    private lateinit var dialogCommon: DialogCommon
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            dialogCommon.showEditCancel(supportFragmentManager, {
                //TODO 계속 편집
            }, {
                //TODO 편집 취소
                goMain()
            })
        }
    }

    private lateinit var resultBitmap: Bitmap

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityEditFreePrintBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(callback)
        dialogCommon = DialogCommon()

        binding.actionbar.actionBack.setOnClickListener {
            dialogCommon.showEditCancel(supportFragmentManager, {
                //TODO 계속 편집
            }, {
                //TODO 편집 취소
                goMain()
            })
        }

        binding.actionbar.actionTitle.text = "인쇄 편집"
        binding.actionbar.actionEtc.text = "인쇄"
        binding.actionbar.actionEtc.setOnClickListener {
            mPhotoEditor.clearHelperBox()

            if (AppDataPref.isFreePreView) {
                goPreview()
            } else {
                resultBitmap = CommonFunc.getBitmapFromView(binding.resultImg)
                FreeImageStorage.bitmap = resultBitmap


            }
        }

        //에디터 초기화
        mPhotoEditorView = binding.photoEditorView
        mPhotoEditor = PhotoEditor.Builder(this, mPhotoEditorView)
        .setPinchTextScalable(true)
            .build()
        mPhotoEditor.setOnPhotoEditorListener(this)
        mPhotoEditorView.source.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                android.R.color.transparent
            )
        )

        setNavigationView()
    }

    private fun setNavigationView() {
        binding.bottomNavigation.menu.findItem(R.id.edit_1).setCheckable(false)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            initStateEditor()

            when (item.itemId) {
                R.id.edit_1 -> {
                    selectImage()
                    true
                }

                R.id.edit_2 -> {
                    binding.textEditView.root.visibility = View.VISIBLE

                    val styleBuilder = TextStyleBuilder()
                    styleBuilder.withTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.color_black
                        )
                    )

                    mPhotoEditor.addEditText(styleBuilder)

                    setEditText()
                    true
                }

                R.id.edit_3 -> {
                    binding.iconView.root.visibility = View.VISIBLE
                    setStickerIcon()
                    true
                }

                R.id.edit_4 -> {
                    mPhotoEditor.addDrawing()
                    true
                }

                R.id.edit_5 -> {
                    binding.etcSettingView.root.visibility = View.VISIBLE
                    setEtcSetting()
                    true
                }

                else -> false
            }
        }

    }

    private fun initStateEditor() {
        mPhotoEditor.clearHelperBox()
        mPhotoEditor.setBrushDrawingMode(false)
        hideKeyBoard()
        binding.iconView.root.visibility = View.GONE
        binding.etcSettingView.root.visibility = View.GONE
        binding.textEditView.root.visibility = View.GONE
    }

    private fun selectImage() {
        val intent = Intent(this, SelectFreePrintActivity::class.java)
        selectImageLauncher.launch(intent)
    }

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val bitmap = FreeImageStorage.bitmap
                bitmap?.let {
                    mPhotoEditor.addFreeImage(this, it)
                }
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                val croppedBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(it))
                croppedBitmap?.let { bitmap ->
                    // 크롭된 이미지를 추가
                    mPhotoEditor.addFreeImage(this, bitmap)
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            cropError?.printStackTrace()
        }
    }

    private fun setEditText() {
        binding.textEditView.editText.setOnClickListener {
            binding.textEditView.editText.isSelected = true
            binding.textEditView.editFont.isSelected = false

            binding.textEditView.editView.visibility = View.VISIBLE
            binding.textEditView.fontEditView.visibility = View.GONE

            hideKeyBoard()
        }

        binding.textEditView.editFont.setOnClickListener {
            binding.textEditView.editText.isSelected = false
            binding.textEditView.editFont.isSelected = true

            binding.textEditView.editView.visibility = View.GONE
            binding.textEditView.fontEditView.visibility = View.VISIBLE

            hideKeyBoard()
        }

        // 텍스트 편집
        val items: List<String> = LinkedList(mutableListOf("10", "12", "14", "16", "18", "20", "24"))
        binding.textEditView.sizeSpinner.attachDataSource(items)
        binding.textEditView.sizeSpinner.selectedIndex = 6
        binding.textEditView.sizeSpinner.setTextColor(ContextCompat.getColor(this, R.color.color_white))
        binding.textEditView.sizeSpinner.onSpinnerItemSelectedListener =
            OnSpinnerItemSelectedListener { _, _, position, _ ->
                binding.textEditView.sizeSpinner.setTextColor(ContextCompat.getColor(this, R.color.color_white))

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

        binding.textEditView.horizontalBtn.setOnClickListener {
            val focusedEditText = currentFocus as? AppCompatEditText
            binding.textEditView.horizontalBtn.isSelected = true
            binding.textEditView.verticalBtn.isSelected = false

            focusedEditText?.let {
                val verticalText = it.text.toString()
                val horizontalText = verticalText.replace("\n", "")
                it.setText(horizontalText)
            }

        }

        binding.textEditView.verticalBtn.setOnClickListener {
            val focusedEditText = currentFocus as? AppCompatEditText
            binding.textEditView.horizontalBtn.isSelected = false
            binding.textEditView.verticalBtn.isSelected = true

            focusedEditText?.let {
                val originalText = it.text.toString()
                val verticalText = originalText.toCharArray().joinToString("\n")
                it.setText(verticalText)
            }

        }

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

        binding.textEditView.styleBtn.setOnClickListener {
            val focusedEditText = currentFocus as? AppCompatEditText
            focusedEditText?.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }
    }

    private fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let { view ->
            imm.hideSoftInputFromWindow(view.windowToken, 0)
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

        val freeAdapter = FreeStickerAdapter(this)
        binding.iconView.recyclerIcon.adapter = freeAdapter
        binding.iconView.recyclerIcon.setHasFixedSize(true)
        freeAdapter.setIconListener(this)
    }

    private fun clearItemDecorations(recyclerView: RecyclerView) {
        val decorationCount = recyclerView.itemDecorationCount
        for (i in decorationCount - 1 downTo 0) {
            recyclerView.removeItemDecorationAt(i)
        }
    }

    override fun onIconClick(bitmap: Bitmap) {
        mPhotoEditor.addImage(bitmap)
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

    override fun onEditTextChangeListener(rootView: View, text: String, colorCode: Int) {

    }

    override fun onAddViewListener(viewType: ViewType, numberOfAddedViews: Int) {

    }

    override fun onRemoveViewListener(viewType: ViewType, numberOfAddedViews: Int) {

    }

    override fun onStartViewChangeListener(viewType: ViewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [$viewType]")
    }

    override fun onStopViewChangeListener(viewType: ViewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [$viewType]")
    }

    override fun onTouchSourceImage(event: MotionEvent) {
        Log.d(TAG, "onTouchView() called with: event = [$event]")
    }

    private fun goTagSetting() {
        val intent = Intent(this, FreePrintTagSettingActivity::class.java)
        startActivity(intent)
    }

    private fun goPreview() {
        resultBitmap = CommonFunc.getBitmapFromView(binding.resultImg)
        FreeImageStorage.bitmap = resultBitmap

        val intent = Intent(this, FreePrintPreviewActivity::class.java)
        startActivity(intent)
    }

    private fun goMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}