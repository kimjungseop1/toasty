package com.syncrown.arpang.ui.component.home.tab1_home.life2cut.preview

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityTwoCutPreviewBinding
import com.syncrown.arpang.databinding.BottomSheetAnotherPaperBinding
import com.syncrown.arpang.databinding.BottomSheetCartridgeBinding
import com.syncrown.arpang.databinding.BottomSheetPaperDisconnectBinding
import com.syncrown.arpang.databinding.BottomSheetPrinterDisconnectBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.DialogToastingCommon
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.TwoCutImageStorage

class TwoCutPreviewActivity : BaseActivity() {
    private lateinit var binding: ActivityTwoCutPreviewBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityTwoCutPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }
        binding.actionbar.actionTitle.text = "미리보기"
        binding.actionbar.actionEtc.text = "인쇄"
        binding.actionbar.actionEtc.setOnClickListener {
            AppDataPref.isTwoCutPreView = binding.previewBtn.isChecked
            AppDataPref.save(this)

            //TODO 프린터 연결 + 용지 장착
            setPrinterAndPaper()

            //TODO 프린터 연결 + 용지 불일치
            setPrinterAndAnotherPaper()

            //TODO 프린터 미연동
            setDisconnectPrinter()

            //TODO 프린터 연결 + 용지 미장착
            setPrinterAndNotPaper()
        }

        Glide.with(this)
            .load(TwoCutImageStorage.bitmap)
            .into(binding.resultImg)

        binding.previewBtn.isChecked = AppDataPref.isTwoCutPreView
    }

    private fun setPrinterAndPaper() {
        val binding = BottomSheetCartridgeBinding.inflate(layoutInflater)
        val bottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setContentView(binding.root)

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.isDraggable = false
        behavior.isHideable = false

        binding.concentration1.isSelected = true
        binding.printType1.isSelected = true
        binding.onePaper.isSelected = true

        binding.concentration1.setOnClickListener {
            binding.concentration1.isSelected = true
            binding.concentration2.isSelected = false
            binding.concentration3.isSelected = false
        }

        binding.concentration2.setOnClickListener {
            binding.concentration1.isSelected = false
            binding.concentration2.isSelected = true
            binding.concentration3.isSelected = false
        }

        binding.concentration3.setOnClickListener {
            binding.concentration1.isSelected = false
            binding.concentration2.isSelected = false
            binding.concentration3.isSelected = true
        }

        binding.printType1.setOnClickListener {
            binding.printType1.isSelected = true
            binding.printType2.isSelected = false
        }

        binding.printType2.setOnClickListener {
            binding.printType1.isSelected = false
            binding.printType2.isSelected = true
        }

        binding.onePaper.setOnClickListener {
            binding.onePaper.isSelected = true
            binding.twoPaper.isSelected = false
        }

        binding.twoPaper.setOnClickListener {
            binding.onePaper.isSelected = false
            binding.twoPaper.isSelected = true
        }

        binding.closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.submitBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
            showToasting()
        }

        bottomSheetDialog.show()
    }

    private fun setPrinterAndAnotherPaper() {
        val binding = BottomSheetPaperDisconnectBinding.inflate(layoutInflater)
        val bottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setContentView(binding.root)

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.isDraggable = false
        behavior.isHideable = false

        binding.closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.submitBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
            showToasting()
        }

        bottomSheetDialog.show()
    }

    private fun setDisconnectPrinter() {
        val binding = BottomSheetPrinterDisconnectBinding.inflate(layoutInflater)
        val bottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setContentView(binding.root)

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.isDraggable = false
        behavior.isHideable = false

        binding.closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.submitBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
            showToasting()
        }

        bottomSheetDialog.show()
    }

    private fun setPrinterAndNotPaper() {
        val binding = BottomSheetAnotherPaperBinding.inflate(layoutInflater)
        val bottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setContentView(binding.root)

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.isDraggable = false
        behavior.isHideable = false

        Glide.with(this)
            .load(TwoCutImageStorage.bitmap)
            .into(binding.contentResultImg)

        binding.closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.nextBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.submitBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
            showToasting()
        }

        bottomSheetDialog.show()
    }

    private fun showToasting() {
        val dialogToast = DialogToastingCommon()
        dialogToast.showLoading(supportFragmentManager)
    }
}