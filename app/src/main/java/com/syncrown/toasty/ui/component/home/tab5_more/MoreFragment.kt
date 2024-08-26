package com.syncrown.toasty.ui.component.home.tab5_more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.syncrown.toasty.databinding.FragmentMoreBinding

class MoreFragment : Fragment() {
    private lateinit var binding: FragmentMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(layoutInflater)

        return binding.root
    }
}