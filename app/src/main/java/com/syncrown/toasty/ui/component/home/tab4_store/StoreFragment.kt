package com.syncrown.toasty.ui.component.home.tab4_store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.syncrown.toasty.databinding.FragmentStoreBinding

class StoreFragment : Fragment() {
    private lateinit var binding: FragmentStoreBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoreBinding.inflate(layoutInflater)

        return binding.root
    }
}