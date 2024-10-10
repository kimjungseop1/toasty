package com.syncrown.arpang.ui.component.home.tab1_home.festival_sticker

import BottomTemplateAdapter
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityFestivalEditBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import kotlinx.coroutines.launch


class EditFestivalActivity : BaseActivity(), BottomTemplateAdapter.TemplateListener {
    private lateinit var binding: ActivityFestivalEditBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityFestivalEditBinding.inflate(layoutInflater)
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

        //에디트 텍스트 편집시 템플릿선택창, 기타설정창 없어지도록
        binding.root.isKeyboardVisible { isVisible ->
            if (isVisible) {
                binding.bottomNavigation.menu.findItem(R.id.edit_1).setCheckable(false)
                binding.bottomNavigation.menu.findItem(R.id.edit_2).setCheckable(false)
                binding.templateView.root.visibility = View.GONE
                binding.etcSettingView.root.visibility = View.GONE
                binding.textEditView.root.visibility = View.VISIBLE
            } else {
                hideKeyBoard()
            }

            setEditView()
        }
    }

    private fun setEditView() {
        binding.textEditView.editText.setOnClickListener {
            binding.textEditView.editText.isSelected = true
            binding.textEditView.fontEditView.isSelected = false

            binding.textEditView.editView.visibility = View.VISIBLE
            binding.textEditView.fontEditView.visibility = View.GONE

            hideKeyBoard()
        }

        binding.textEditView.editFont.setOnClickListener {
            binding.textEditView.editText.isSelected = false
            binding.textEditView.fontEditView.isSelected = true

            binding.textEditView.editText.visibility = View.GONE
            binding.textEditView.fontEditView.visibility = View.VISIBLE

            hideKeyBoard()
        }

        //현재 선택되어진 에디트뷰
        val focusedEditText = currentFocus as? AppCompatEditText

        // 텍스트 편집
        val items = arrayOf("10","12","14","16","18")
        val myAdapter = ArrayAdapter(this, R.layout.spinner_item_edit_text_size, items)
        binding.textEditView.sizeSpinner.adapter = myAdapter
        binding.textEditView.sizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val textSize = when (position) {
                    0 -> 10f
                    1 -> 12f
                    2 -> 14f
                    3 -> 16f
                    4 -> 18f
                    else -> 16f
                }

                focusedEditText?.textSize = textSize
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.textEditView.horizontalBtn.setOnClickListener {
            binding.textEditView.horizontalBtn.isSelected = true
            binding.textEditView.verticalBtn.isSelected = false

            focusedEditText?.let {
                val verticalText = it.text.toString()
                val horizontalText = verticalText.replace("\n", "")
                it.setText(horizontalText)
            }

        }

        binding.textEditView.verticalBtn.setOnClickListener {
            binding.textEditView.horizontalBtn.isSelected = false
            binding.textEditView.verticalBtn.isSelected = true

            focusedEditText?.let {
                val originalText = it.text.toString()
                val verticalText = originalText.toCharArray().joinToString("\n")
                it.setText(verticalText)
            }

        }

        binding.textEditView.alignLeftBtn.setOnClickListener {
            binding.textEditView.alignLeftBtn.isSelected = true
            binding.textEditView.alignCenterBtn.isSelected = false
            binding.textEditView.alignRightBtn.isSelected = false

            focusedEditText?.gravity = Gravity.LEFT
        }

        binding.textEditView.alignCenterBtn.setOnClickListener {
            binding.textEditView.alignLeftBtn.isSelected = false
            binding.textEditView.alignCenterBtn.isSelected = true
            binding.textEditView.alignRightBtn.isSelected = false

            focusedEditText?.gravity = Gravity.CENTER
        }

        binding.textEditView.alignRightBtn.setOnClickListener {
            binding.textEditView.alignLeftBtn.isSelected = false
            binding.textEditView.alignCenterBtn.isSelected = false
            binding.textEditView.alignRightBtn.isSelected = true

            focusedEditText?.gravity = Gravity.RIGHT
        }

        binding.textEditView.thinBtn.setOnClickListener {
            binding.textEditView.thinBtn.isSelected = true
            binding.textEditView.boldBtn.isSelected = false

            focusedEditText?.typeface = Typeface.DEFAULT
        }

        binding.textEditView.boldBtn.setOnClickListener {
            binding.textEditView.thinBtn.isSelected = false
            binding.textEditView.boldBtn.isSelected = true

            focusedEditText?.typeface = Typeface.DEFAULT_BOLD
        }

        binding.textEditView.styleBtn.setOnClickListener {
            focusedEditText?.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }
    }

    private fun setNavigationView() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            binding.bottomNavigation.menu.findItem(R.id.edit_1).setCheckable(true)
            binding.bottomNavigation.menu.findItem(R.id.edit_2).setCheckable(true)
            binding.templateView.root.visibility = View.GONE
            binding.etcSettingView.root.visibility = View.GONE
            binding.textEditView.root.visibility = View.GONE
            hideKeyBoard()

            when (item.itemId) {
                R.id.edit_1 -> {

                    if (binding.templateView.root.visibility == View.VISIBLE) {
                        binding.templateView.root.visibility = View.GONE
                    } else {
                        binding.templateView.root.visibility = View.VISIBLE
                        setTemplateView()
                    }
                    true
                }

                R.id.edit_2 -> {

                    if (binding.etcSettingView.root.visibility == View.VISIBLE) {
                        binding.etcSettingView.root.visibility = View.GONE
                    } else {
                        binding.etcSettingView.root.visibility = View.VISIBLE
                    }

                    true
                }

                else -> false
            }
        }
    }

    private fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let { view ->
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun View.isKeyboardVisible(listener: (Boolean) -> Unit) {
        val rootView = this.rootView
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            private var isKeyboardVisible = false

            override fun onGlobalLayout() {
                val rect = Rect()
                rootView.getWindowVisibleDisplayFrame(rect)

                val screenHeight = rootView.height
                val keypadHeight = screenHeight - rect.bottom

                val isVisible = keypadHeight > screenHeight * 0.15

                if (isVisible != isKeyboardVisible) {
                    isKeyboardVisible = isVisible
                    listener(isKeyboardVisible)
                }
            }
        })
    }

    private fun setTemplateView() {
        clearItemDecorations(binding.templateView.recyclerTemp)

        val gridLayoutManager = GridLayoutManager(this, 4)
        binding.templateView.recyclerTemp.layoutManager = gridLayoutManager
        binding.templateView.recyclerTemp.addItemDecoration(
            GridSpacingItemDecoration(
                4,
                CommonFunc.dpToPx(5, this),
                false
            )
        )

        val templateAdapter = BottomTemplateAdapter(this)
        binding.templateView.recyclerTemp.adapter = templateAdapter
        binding.templateView.recyclerTemp.setHasFixedSize(true)
        templateAdapter.setTemplateListener(this)
    }

    private fun clearItemDecorations(recyclerView: RecyclerView) {
        val decorationCount = recyclerView.itemDecorationCount
        for (i in decorationCount - 1 downTo 0) {
            recyclerView.removeItemDecorationAt(i)
        }
    }

    override fun onTemplateClick(bitmap: Bitmap) {
        Glide.with(this)
            .load(bitmap)
            .circleCrop()
            .into(binding.templateImg)
    }
}