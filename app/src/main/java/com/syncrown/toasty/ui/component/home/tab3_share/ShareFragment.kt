package com.syncrown.toasty.ui.component.home.tab3_share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.syncrown.toasty.databinding.FragmentShareBinding

class ShareFragment : Fragment() {
    private lateinit var binding: FragmentShareBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShareBinding.inflate(layoutInflater)

        return binding.root
    }
}