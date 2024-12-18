package com.syncrown.arpang.ui.component.home.tab1_home.main.use

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.databinding.FragmentCase2Binding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestMainBannerDto
import com.syncrown.arpang.network.model.ResponseMainBannerDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.HorizontalSpaceItemDecoration
import com.syncrown.arpang.ui.component.home.tab1_home.main.adapter.CaseListAdapter
import kotlinx.coroutines.launch

class Case2Fragment : Fragment() {
    private lateinit var binding: FragmentCase2Binding
    private val case2ViewModel: CaseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCase2Binding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            setTwoCutMainBannerData()

            observeData()
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            case2ViewModel.twoCutBannerResponseLiveData().observe(viewLifecycleOwner) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val data = result.data?.root
                        data?.let {
                            setContentList(it)
                        }
                    }

                    is NetworkResult.NetCode -> {
                        Log.e("jung", "실패 : ${result.message}")
                        if (result.message.equals("403")) {
                            (activity as? BaseActivity)?.goLogin()
                        }
                    }

                    is NetworkResult.Error -> {
                        Log.e("jung", "오류 : ${result.message}")
                    }
                }
            }
        }
    }

    private fun setTwoCutMainBannerData() {
        val requestMainBannerDto = RequestMainBannerDto().apply {
            banner_se_code = "AR_MAIN_USE"
            menu_code = "AR_MENU02"
        }

        case2ViewModel.twoCutBanner(requestMainBannerDto)
    }

    private fun setContentList(data: ArrayList<ResponseMainBannerDto.Root>) {
        if (data.size > 0) {
            binding.recyclerCase.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE

            binding.recyclerCase.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            val caseAdapter = CaseListAdapter(requireContext(), data)
            caseAdapter.setOnItemClickListener(object : CaseListAdapter.OnItemClickListener {
                override fun onClick(position: Int, clickLink: String) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(clickLink))
                    startActivity(intent)
                }
            })
            binding.recyclerCase.adapter = caseAdapter

            binding.recyclerCase.addItemDecoration(
                HorizontalSpaceItemDecoration(
                    CommonFunc.dpToPx(
                        12,
                        requireContext()
                    )
                )
            )
        } else {
            binding.recyclerCase.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        }
    }
}