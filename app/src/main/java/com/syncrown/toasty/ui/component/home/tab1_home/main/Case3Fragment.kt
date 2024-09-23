package com.syncrown.toasty.ui.component.home.tab1_home.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.syncrown.toasty.databinding.FragmentCase3Binding

class Case3Fragment : Fragment() {
    private lateinit var binding: FragmentCase3Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCase3Binding.inflate(layoutInflater)

        return binding.root
    }
}