package com.syncrown.arpang.ui.component.home.tab1_home.connect_device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.syncrown.arpang.databinding.FragmentConnectDevice1Binding
import com.syncrown.arpang.databinding.FragmentHomeBinding

class GuideUse1Fragment : Fragment() {
    private lateinit var binding: FragmentConnectDevice1Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConnectDevice1Binding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}