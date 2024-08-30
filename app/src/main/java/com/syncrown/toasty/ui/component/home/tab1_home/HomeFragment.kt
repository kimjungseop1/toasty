package com.syncrown.toasty.ui.component.home.tab1_home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.FragmentHomeBinding
import com.syncrown.toasty.ui.component.home.MainViewModel
import com.syncrown.toasty.ui.component.home.tab1_home.adapter.SlideBannerAdapter

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: MainViewModel by activityViewModels()

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        showSlideBanner()

        return binding.root
    }

    private val slideRunnable =
        Runnable { binding.viewpager.currentItem += 1 }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(slideRunnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(slideRunnable, 3000)
    }

    private fun showSlideBanner() {
        val arrayList = ArrayList<String>()
        arrayList.add("1")
        arrayList.add("2")
        arrayList.add("3")
        arrayList.add("4")

        val pagerWidth = resources.getDimensionPixelOffset(R.dimen.pagerWidth)
        val screenWidth = resources.displayMetrics.widthPixels
        val pagerPadding = ((screenWidth - pagerWidth) * 0.5).toInt()
        val offsetPx = ((screenWidth - pagerWidth) * 0.25).toInt()

        val bannerAdapter = SlideBannerAdapter(requireContext(), binding.viewpager, arrayList)
        binding.viewpager.apply {
            adapter = bannerAdapter
            offscreenPageLimit = arrayList.size
            setPadding(pagerPadding, 0, pagerPadding, 0)
            setPageTransformer { page, position ->
                page.translationX = position * offsetPx
            }
            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                @SuppressLint("SetTextI18n")
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (arrayList.size > 1) {
                        handler.removeCallbacks(slideRunnable)
                        handler.postDelayed(slideRunnable, 3000)
                    }

//                    binding.countTxt.text = binding.viewpager.currentItem.toString() + "/" + arrayList.size
                }
            })
        }

        handler.postDelayed(slideRunnable, 3000)
    }
}