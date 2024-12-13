package com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videotrimmer

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.OptIn
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ClippingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityTrimVideoBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.ActivityFinishManager
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.ArImageStorage
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.edit.EditVideoPrintActivity
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videotrimmer.view.VideoTrimmerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@SuppressLint("UnsafeOptInUsageError")
class TrimVideoActivity : BaseActivity(), VideoTrimmerView.OnSelectedRangeChangedListener {
    private lateinit var binding: ActivityTrimVideoBinding

    private val REQ_PERMISSION = 200

    private var startMillis: Long = 0
    private var endMillis: Long = 0

    private val player: ExoPlayer by lazy {
        ExoPlayer.Builder(this).build().also {
            it.repeatMode = ExoPlayer.REPEAT_MODE_OFF
            binding.playerView.player = it
        }
    }

    private val dataSourceFactory: DefaultDataSource.Factory by lazy {
        DefaultDataSource.Factory(
            this,
            DefaultHttpDataSource.Factory().setUserAgent("VideoTrimmer")
        )
    }

    private var videoPath: String = ""

    override fun observeViewModel() {
        // 다음화면인 인쇄편집에서 영상변경 선택시 현재 액티비티로 오면 안됨
        ActivityFinishManager.finishActivityEvent.observe(this) { activityClass ->
            if (activityClass == this::class.java) {
                finish()
                ActivityFinishManager.finishActivityEvent.postValue(null)
            }
        }
    }

    override fun initViewBinding() {
        binding = ActivityTrimVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.actionbar.actionTitle.text = getString(R.string.edit_video_print_edit_title)
        binding.actionbar.actionEtc.text = getString(R.string.edit_crop_image_ok)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener { finish() }

        binding.actionbar.actionEtc.setOnClickListener {
            if (videoPath.isNotEmpty() || startMillis != 0L || endMillis != 0L) {
                val isSave = binding.videoTrimmerView.saveSelectedVideoRangeToCacheFolder(
                    this,
                    File(videoPath),
                    startMillis,
                    endMillis
                )

                if (isSave.first) {
                    Log.e("jung","영상 클립 완료")
                    // 인쇄 편집으로 이동
                    val filePath = isSave.second
                    ArImageStorage.resultVideoPath = filePath.toString()
                    goEditVideoPrint(filePath)
                } else {
                    Log.e("jung","영상 클립 실패")
                }
            }
        }

        videoPath = intent.getStringExtra("VIDEO_FILE_PATH").toString()
        if (videoPath.isNotEmpty()) {
            lifecycleScope.launch {
                delay(100)
                if (!isFinishing) {
                    displayTrimmerView(videoPath)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
        player.clearMediaItems()
        player.release()
    }

    override fun onStart() {
        super.onStart()
        // Android 13 이상인지 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissions = mutableListOf<String>()
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_VIDEO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.READ_MEDIA_VIDEO)
            }
            if (permissions.isNotEmpty()) {
                ActivityCompat.requestPermissions(this, permissions.toTypedArray(), REQ_PERMISSION)
            }
        } else {
            // Android 13 미만에서는 기존 방식 사용
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQ_PERMISSION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQ_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // 권한이 부여된 경우
                // 여기에 권한이 부여되었을 때 수행할 동작을 추가할 수 있습니다.
            } else {
                // 권한이 거부된 경우 앱을 종료합니다.
                finish()
            }
        }
    }

    override fun onSelectRangeStart() {
        player.playWhenReady = false
    }

    override fun onSelectRange(startMillis: Long, endMillis: Long) {
        this.startMillis = startMillis
        this.endMillis = endMillis
    }

    override fun onSelectRangeEnd(startMillis: Long, endMillis: Long) {
        binding.videoTrimmerView.setSelectedRange(startMillis, endMillis)
        playVideo(videoPath, startMillis, endMillis)
    }

    private fun displayTrimmerView(path: String) {
        binding.videoTrimmerView
            .setVideo(File(path))
            .setMaxDuration(10000)
            .setMinDuration(1000)
            .setFrameCountInWindow(10)
            .setExtraDragSpace(CommonFunc.dpToPx(2f, this))
            .setOnSelectedRangeChangedListener(this)
            .show()
    }

    @OptIn(UnstableApi::class)
    private fun playVideo(path: String, startMillis: Long, endMillis: Long) {
        if (path.isBlank()) return

        val mediaItem = MediaItem.fromUri(Uri.parse(path))
        val source = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)
            .let {
                ClippingMediaSource(
                    it,
                    startMillis * 1000L,
                    endMillis * 1000L
                )
            }

        player.setMediaSource(source)

        binding.videoTrimmerView.setExoPlayer(player)

        player.playWhenReady = true
        player.prepare()

    }

    private fun goEditVideoPrint(filePath: String?) {
        player.stop()
        player.release()

        val intent = Intent(this, EditVideoPrintActivity::class.java)
        intent.putExtra("CACHE_FILE_PATH", filePath)
        startActivity(intent)
        finish()
    }
}