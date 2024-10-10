package com.syncrown.arpang.ui.component.home.tab1_home.label_sticker

import BottomIconAdapter
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityEditLabelStickerBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import kotlinx.coroutines.launch

class EditLabelStickerActivity : BaseActivity(), BottomIconAdapter.IconListener {
    private lateinit var binding: ActivityEditLabelStickerBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityEditLabelStickerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = "인쇄 편집"
        binding.actionbar.actionEtc.text = "인쇄"
        binding.actionbar.actionEtc.setOnClickListener {

        }

        lifecycleScope.launch {
            setNavigationView()

            binding.bottomNavigation.selectedItemId = R.id.edit_1
        }

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
                    }
                    true
                }

                R.id.edit_3 -> {
                    if (binding.etcSettingView.root.visibility == View.VISIBLE) {
                        binding.etcSettingView.root.visibility = View.GONE
                    } else {
                        binding.etcSettingView.root.visibility = View.VISIBLE
                        setEtcSetting()
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
        val items = arrayOf("10", "12", "14", "16", "18", "20", "24")
        val myAdapter = ArrayAdapter(this, R.layout.spinner_item_edit_text_size, items)
        binding.textEditView.sizeSpinner.adapter = myAdapter
        binding.textEditView.sizeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
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

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
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

    private fun setEtcSetting() {

    }


}