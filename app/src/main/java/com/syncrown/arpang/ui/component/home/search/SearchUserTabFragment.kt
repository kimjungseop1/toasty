package com.syncrown.arpang.ui.component.home.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.syncrown.arpang.databinding.FragmentConnectDevice1Binding
import com.syncrown.arpang.databinding.FragmentHomeBinding
import com.syncrown.arpang.databinding.FragmentSearchUserBinding

class SearchUserTabFragment : Fragment() {
    private lateinit var binding: FragmentSearchUserBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchUserBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}