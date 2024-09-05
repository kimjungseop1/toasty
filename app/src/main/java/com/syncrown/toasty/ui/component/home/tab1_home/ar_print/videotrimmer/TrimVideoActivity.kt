package com.syncrown.toasty.ui.component.home.tab1_home.ar_print.videotrimmer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ClippingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.syncrown.toasty.databinding.ActivityTrimVideoBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.commons.CommonFunc
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.videotrimmer.tools.convertMillisToMinutesSeconds
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.videotrimmer.view.VideoTrimmerView
import java.io.File

@UnstableApi
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

    }

    override fun initViewBinding() {
        binding = ActivityTrimVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.actionbar.actionTitle.text = "영상 조정"
        binding.actionbar.actionEtc.text = "확인"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionEtc.setOnClickListener {
            if (videoPath.isNotEmpty() || startMillis != 0L || endMillis != 0L) {
                val isSave = binding.videoTrimmerView.saveSelectedVideoRangeToDownloadFolder(
                    this,
                    File(videoPath),
                    startMillis,
                    endMillis
                )

                if (isSave) {
                    Toast.makeText(this, "save success", Toast.LENGTH_SHORT).show()

                    // 인쇄 편집으로 이동


                } else {
                    Toast.makeText(this, "save fail", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.pickVideoBtn.setOnClickListener {
            pickVideo()
        }
    }

    private val pickVideoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            videoPath = getRealPathFromMediaData(data?.data)
            Log.e("jung", "path : $videoPath, " + data?.data + ", " + File(videoPath).absolutePath)
            displayTrimmerView(videoPath)
        }
    }

    private fun pickVideo() {
        Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI).apply {
            type = "video/*"
        }.also { pickVideoLauncher.launch(it) }
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

    /* -------------------------------------------------------------------------------------------*/
    /* VideoTrimmerView.OnSelectedRangeChangedListener */
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

    /* -------------------------------------------------------------------------------------------*/
    /* VideoTrimmer */
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

    /* -------------------------------------------------------------------------------------------*/
    /* ExoPlayer3 */
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

    /* -------------------------------------------------------------------------------------------*/
    /* Internal helpers */
    private fun getRealPathFromMediaData(data: Uri?): String {
        data ?: return ""

        var cursor: Cursor? = null
        try {
            cursor = contentResolver.query(
                data,
                arrayOf(MediaStore.Video.Media.DATA),
                null, null, null
            )

            val col = cursor?.getColumnIndex(MediaStore.Video.Media.DATA) ?: -1
            cursor?.moveToFirst()

            return cursor?.getString(col) ?: ""
        } finally {
            cursor?.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}