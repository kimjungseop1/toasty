package com.syncrown.toasty.ui.component.home.tab2_Lib

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.syncrown.toasty.databinding.FragmentLibBinding

class LibFragment : Fragment() {
    private lateinit var binding: FragmentLibBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibBinding.inflate(layoutInflater)

        return binding.root
    }
}