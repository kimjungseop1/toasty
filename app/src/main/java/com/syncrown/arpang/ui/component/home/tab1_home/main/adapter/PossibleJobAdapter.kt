package com.syncrown.arpang.ui.component.home.tab1_home.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ItemPossibleJobBinding
import com.syncrown.arpang.network.model.ResponseAppMainDto

class PossibleJobAdapter(
    private val context: Context,
    private val arrayList: ArrayList<ResponseAppMainDto.Root>
) : RecyclerView.Adapter<PossibleJobAdapter.ViewHolder>() {
    private var mJobListener: JobListener? = null

    fun setJobListener(jobListener: JobListener) {
        mJobListener = jobListener
    }

    interface JobListener {
        fun onJobClick(position: Int, code: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PossibleJobAdapter.ViewHolder {
        val binding =
            ItemPossibleJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PossibleJobAdapter.ViewHolder, position: Int) {
        holder.onBind(context, position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(val binding: ItemPossibleJobBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(context: Context, position: Int) {
            val code = arrayList[position].code
            when (code) {
                "AR_MENU01" -> handleMenu01(context, position)
                "AR_MENU02" -> handleMenu02(context, position)
                "AR_MENU03" -> handleMenu03(context, position)
                "AR_MENU04" -> handleMenu04(context, position)
                "AR_MENU05" -> handleMenu05(context, position)
            }

            binding.root.setOnClickListener {
                mJobListener?.onJobClick(position, code.toString())
            }
        }

        private fun handleMenu01(context: Context, position: Int) {
            binding.codeName.text = arrayList[position].code_nm

            Glide.with(context).load(R.drawable.bg_ar_home).into(binding.codeImg)
        }

        private fun handleMenu02(context: Context, position: Int) {
            binding.codeName.text = arrayList[position].code_nm

            Glide.with(context).load(R.drawable.bg_4cut_home).into(binding.codeImg)
        }

        private fun handleMenu03(context: Context, position: Int) {
            binding.codeName.text = arrayList[position].code_nm

            Glide.with(context).load(R.drawable.bg_sticker_home).into(binding.codeImg)
        }

        private fun handleMenu04(context: Context, position: Int) {
            binding.codeName.text = arrayList[position].code_nm

            Glide.with(context).load(R.drawable.bg_festival_home).into(binding.codeImg)
        }

        private fun handleMenu05(context: Context, position: Int) {
            binding.codeName.text = arrayList[position].code_nm

            Glide.with(context).load(R.drawable.bg_free_home).into(binding.codeImg)
        }
    }
}
