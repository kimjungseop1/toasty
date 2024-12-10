package com.syncrown.arpang.ui.component.home.tab5_more.alert

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityAlertMainBinding
import com.syncrown.arpang.db.push_db.PushMessageEntity
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.fcm.MessageCountManager
import com.syncrown.arpang.ui.component.home.tab5_more.alert.adapter.AlertListAdapter
import kotlinx.coroutines.launch

class AlertActivity : BaseActivity() {
    private lateinit var binding: ActivityAlertMainBinding
    private val alertViewModel: AlertViewModel by viewModels()

    override fun observeViewModel() {
        lifecycleScope.launch {
            alertViewModel.getTodayMessages().observe(this@AlertActivity) { result ->
                if (result != null) {
                    setTodayList(result)
                }
            }
        }

        lifecycleScope.launch {
            alertViewModel.getWeekMessages().observe(this@AlertActivity) { result ->
                if (result != null) {
                    setWeekList(result)
                }
            }
        }
    }

    override fun initViewBinding() {
        binding = ActivityAlertMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //메시지 카운트 초기화
        MessageCountManager.resetCount()

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.alert_title)

    }

    private fun setTodayList(data: List<PushMessageEntity>) {
        binding.recyclerToday.layoutManager = LinearLayoutManager(this)
        binding.recyclerToday.adapter = AlertListAdapter(this, data)
    }

    private fun setWeekList(data: List<PushMessageEntity>) {
        binding.recyclerWeek.layoutManager = LinearLayoutManager(this)
        binding.recyclerWeek.adapter = AlertListAdapter(this, data)
    }
}