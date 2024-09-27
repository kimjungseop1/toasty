package com.syncrown.toasty.ui.component.home.tab3_share

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.media3.common.util.UnstableApi
import com.syncrown.toasty.databinding.FragmentShareBinding
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.videotrimmer.TrimVideoActivity

class ShareFragment : Fragment() {
    private lateinit var binding: FragmentShareBinding

    @OptIn(UnstableApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShareBinding.inflate(layoutInflater)

        binding.testBtn.setOnClickListener {
            val intent = Intent(requireContext(), TrimVideoActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}