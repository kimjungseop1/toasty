package com.syncrown.arpang.ui.component.home.tab1_home.label_sticker

import BottomIconAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityEditLabelStickerBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.commons.FontManager
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.component.home.MainActivity
import com.syncrown.arpang.ui.component.home.tab1_home.label_sticker.adapter.FontAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.label_sticker.preview.LabelStickerPreviewActivity
import com.syncrown.arpang.ui.component.home.tab1_home.label_sticker.tag.LabelStickerTagSettingActivity
import kotlinx.coroutines.launch
import org.angmarch.views.OnSpinnerItemSelectedListener
import java.util.LinkedList


class EditLabelStickerActivity : BaseActivity(), BottomIconAdapter.IconListener {
    private lateinit var binding: ActivityEditLabelStickerBinding

    private lateinit var dialogCommon: DialogCommon
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            //소프트키 뒤로 버튼 처리
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
        binding = ActivityEditLabelStickerBinding.inflate(layoutInflater)
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
            if (AppDataPref.isLabelPreView) {
                goPreview()
            } else {

            }
        }

        lifecycleScope.launch {
            setNavigationView()

            binding.bottomNavigation.selectedItemId = R.id.edit_1

            setInput()
        }

    }

    private fun setInput() {
        // 텍스트뷰의 길이를 초과할 경우 마지막 입력 문자 제거
        binding.inputText.requestFocus()
        binding.inputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val paint = binding.inputText.paint
                val availableWidth =
                    binding.inputText.width - binding.inputText.paddingLeft - binding.inputText.paddingRight
                val textWidth = paint.measureText(p0.toString())

                if (textWidth > availableWidth) {
                    p0?.delete(p0.length - 1, p0.length)
                }
            }
        })

    }

    private fun setNavigationView() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            binding.bottomNavigation.menu.findItem(R.id.edit_1).setCheckable(true)
            binding.bottomNavigation.menu.findItem(R.id.edit_2).setCheckable(true)
            binding.bottomNavigation.menu.findItem(R.id.edit_3).setCheckable(true)
            binding.iconView.root.visibility = View.GONE
            binding.etcSettingView.root.visibility = View.GONE
            binding.textEditView.root.visibility = View.GONE

            when (item.itemId) {
                R.id.edit_1 -> {
                    if (binding.iconView.root.visibility == View.VISIBLE) {
                        binding.iconView.root.visibility = View.GONE
                    } else {
                        binding.iconView.root.visibility = View.VISIBLE
                        setStickerIcon()
                    }
                    true
                }

                R.id.edit_2 -> {
                    if (binding.textEditView.root.visibility == View.VISIBLE) {
                        binding.textEditView.root.visibility = View.GONE
                    } else {
                        binding.textEditView.root.visibility = View.VISIBLE
                        setEditText()
                        setEditFont()
                    }
                    true
                }

                R.id.edit_3 -> {
                    if (binding.etcSettingView.root.visibility == View.VISIBLE) {
                        binding.etcSettingView.root.visibility = View.GONE
                    } else {
                        binding.etcSettingView.root.visibility = View.VISIBLE
                    }

                    binding.etcSettingView.tagSetting.setOnClickListener {
                        goTagSetting()
                    }

                    binding.etcSettingView.preview.setOnClickListener {
                        goPreview()
                    }
                    true
                }

                else -> false
            }
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

        val templateAdapter = BottomIconAdapter(this)
        binding.iconView.recyclerIcon.adapter = templateAdapter
        binding.iconView.recyclerIcon.setHasFixedSize(true)
        templateAdapter.setIconListener(this)
    }

    private fun clearItemDecorations(recyclerView: RecyclerView) {
        val decorationCount = recyclerView.itemDecorationCount
        for (i in decorationCount - 1 downTo 0) {
            recyclerView.removeItemDecorationAt(i)
        }
    }

    override fun onIconClick(bitmap: Bitmap) {
        Glide.with(this).load(bitmap).into(binding.iconImg)
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
        val adapter = FontAdapter(this,
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

    private fun goPreview() {
        resultBitmap = CommonFunc.getBitmapFromView(binding.contentView)
        LabelStickerImageStorage.bitmap = resultBitmap

        val intent = Intent(this, LabelStickerPreviewActivity::class.java)
        startActivity(intent)
    }

    private fun goTagSetting() {
        val intent = Intent(this, LabelStickerTagSettingActivity::class.java)
        startActivity(intent)
    }

    private fun goMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}