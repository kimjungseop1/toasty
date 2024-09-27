package com.syncrown.arpang.ui.component.join.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.syncrown.arpang.databinding.ActivityEmailJoinBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.join.email_cert.CertEmailActivity
import com.syncrown.arpang.ui.component.join.term_privacy.PolishWebActivity
import kotlinx.coroutines.launch

class JoinEmailActivity : BaseActivity() {
    private lateinit var binding: ActivityEmailJoinBinding
    private val joinEmailViewModel: JoinEmailViewModel by viewModels()

    override fun observeViewModel() {
        lifecycleScope.launch {

        }
    }

    override fun initViewBinding() {
        binding = ActivityEmailJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.inputEmailView.text = intent.getStringExtra("INPUT_EMAIL_ADDRESS").toString()

        binding.inputNameView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    if (p0.length > 1) {
                        binding.warningName.visibility = View.GONE
                    } else {
                        binding.warningName.visibility = View.VISIBLE
                    }

                    binding.joinBtn.isSelected = checkValidation()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.inputPwView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    if (p0.length < 6) {
                        binding.warningLength.visibility = View.VISIBLE
                    } else {
                        binding.warningLength.visibility = View.GONE
                    }
                }

                if (binding.inputPwMoreView.text!!.isNotEmpty()) {
                    if (binding.inputPwView.text.toString()
                            .compareTo(binding.inputPwMoreView.text.toString()) == 0
                    ) {
                        binding.warningPw.visibility = View.GONE
                    } else {
                        binding.warningPw.visibility = View.VISIBLE
                    }
                }

                binding.joinBtn.isSelected = checkValidation()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.pw1ShowBtn.setOnClickListener {
            if (binding.inputPwView.transformationMethod == PasswordTransformationMethod.getInstance()) {
                binding.inputPwView.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding.inputPwView.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }

        binding.inputPwMoreView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.inputPwView.text.toString()
                        .compareTo(binding.inputPwMoreView.text.toString()) == 0
                ) {
                    binding.warningPw.visibility = View.GONE
                } else {
                    binding.warningPw.visibility = View.VISIBLE
                }

                binding.joinBtn.isSelected = checkValidation()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.pw2ShowBtn.setOnClickListener {
            if (binding.inputPwMoreView.transformationMethod == PasswordTransformationMethod.getInstance()) {
                binding.inputPwMoreView.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding.inputPwMoreView.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }

        binding.allConsentBtn.setOnCheckedChangeListener { _, isChecked ->
            binding.consentBtn1.isChecked = isChecked
            binding.consentBtn2.isChecked = isChecked
            binding.consentBtn3.isChecked = isChecked

            binding.joinBtn.isSelected = checkValidation()
        }

        binding.consentBtn1.setOnCheckedChangeListener { _, isChecked ->
            binding.joinBtn.isSelected = checkValidation()
        }

        binding.consentBtn2.setOnCheckedChangeListener { _, isChecked ->
            binding.joinBtn.isSelected = checkValidation()
        }

        binding.consentDetail1.setOnClickListener {
            goWebPolish("term")
            binding.joinBtn.isSelected = checkValidation()
        }

        binding.consentDetail2.setOnClickListener {
            goWebPolish("privacy")
            binding.joinBtn.isSelected = checkValidation()
        }

        binding.joinBtn.isSelected = false
        binding.joinBtn.setOnClickListener {
            if (checkValidation()) {
                goCertEmail()
            }
        }
    }

    private fun checkValidation(): Boolean {
        if (binding.inputNameView.text?.isEmpty() == true) {
            return false
        }

        if (binding.warningName.visibility == View.VISIBLE) {
            return false
        }

        if (binding.inputPwView.text?.isEmpty() == true) {
            return false
        }

        if (binding.inputPwMoreView.text?.isEmpty() == true) {
            return false
        }

        if (binding.warningPw.visibility == View.VISIBLE) {
            return false
        }

        if (!binding.consentBtn1.isChecked) {
            return false
        }

        if (!binding.consentBtn2.isChecked) {
            return false
        }

        return true
    }

    private fun goCertEmail() {
        val intent = Intent(this, CertEmailActivity::class.java)
        startActivity(intent)
    }

    private fun goWebPolish(extra: String) {
        val intent = Intent(this, PolishWebActivity::class.java)
        intent.putExtra("FROM_JOIN_EMAIL", 1)
        intent.putExtra("CONSENT_EXTRA", extra)
        webResultLauncher.launch(intent)
    }

    private val webResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == RESULT_OK) {
                val actPos = result.data?.getIntExtra("WEB_RESULT_DATA", 0)
                if (actPos == 1) {
                    binding.consentBtn1.performClick()
                }

                if (actPos == 2) {
                    binding.consentBtn2.performClick()
                }

                if (checkValidation()) {
                    binding.joinBtn.isSelected = true
                }
            }
        }

}