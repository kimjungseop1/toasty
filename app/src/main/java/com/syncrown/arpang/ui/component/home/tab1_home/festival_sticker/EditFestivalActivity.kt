package com.syncrown.arpang.ui.component.home.tab1_home.festival_sticker

import BottomTemplateAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
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
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityFestivalEditBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.component.home.MainActivity
import com.syncrown.arpang.ui.component.home.tab1_home.festival_sticker.preview.FestivalPreviewActivity
import com.syncrown.arpang.ui.component.home.tab1_home.festival_sticker.tag.FestivalTagSettingActivity
import com.syncrown.arpang.ui.component.home.tab1_home.free_print.FreeImageStorage
import kotlinx.coroutines.launch
import org.angmarch.views.OnSpinnerItemSelectedListener
import java.util.LinkedList


class EditFestivalActivity : BaseActivity(), BottomTemplateAdapter.TemplateListener {
    private lateinit var binding: ActivityFestivalEditBinding

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
        binding = ActivityFestivalEditBinding.inflate(layoutInflater)
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
            if (AppDataPref.isFestivalPreView) {
                goPreview()
            } else {
                resultBitmap = CommonFunc.getBitmapFromView(binding.festivalView)
                FestivalImageStorage.bitmap = resultBitmap

            }
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
        }
    }

    private fun setEditView() {
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
                    binding.templateView.root.visibility = View.VISIBLE
                    setTemplateView()
                    true
                }

                R.id.edit_2 -> {
                    binding.textEditView.root.visibility = View.VISIBLE
                    setEditView()
                    true
                }

                R.id.edit_3 -> {
                    binding.etcSettingView.root.visibility = View.VISIBLE

                    binding.etcSettingView.publicSwitch.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {

                        }
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

    private fun goTagSetting() {
        val intent = Intent(this, FestivalTagSettingActivity::class.java)
        startActivity(intent)
    }

    private fun goPreview() {
        resultBitmap = CommonFunc.getBitmapFromView(binding.festivalView)
        FestivalImageStorage.bitmap = resultBitmap

        val intent = Intent(this, FestivalPreviewActivity::class.java)
        startActivity(intent)
    }

    private fun goMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}