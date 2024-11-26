package com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videotrimmer.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaMetadataRetriever
import android.media.MediaMuxer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.LayoutVideoTrimmerBinding
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videotrimmer.slidingwindow.SlidingWindowView
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videotrimmer.tools.dpToPx
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videotrimmer.videoframe.VideoFramesAdaptor
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videotrimmer.videoframe.VideoFramesDecoration
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videotrimmer.videoframe.VideoFramesScrollListener
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import kotlin.math.roundToInt

class VideoTrimmerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle), VideoTrimmerContract.View {
    private var binding: LayoutVideoTrimmerBinding =
        LayoutVideoTrimmerBinding.inflate(LayoutInflater.from(context), this, true)

    @DrawableRes
    private var leftBarRes: Int = R.drawable.trimmer_left_bar

    @DrawableRes
    private var rightBarRes: Int = R.drawable.trimmer_right_bar

    private var barWidth: Float = dpToPx(context, 10f)
    private var borderWidth: Float = 0f

    @ColorInt
    private var borderColor: Int = Color.BLACK

    @ColorInt
    private var overlayColor: Int = ContextCompat.getColor(context, R.color.color_50black)

    private var presenter: VideoTrimmerContract.Presenter? = null
    private var adaptor: VideoFramesAdaptor? = null

    private var exoPlayer: ExoPlayer? = null
    private var playheadUpdateHandler: android.os.Handler? = null
    private var playheadUpdateRunnable: Runnable? = null
    private var selectedStartTime: Long = 0L
    private var selectedEndTime: Long = 0L


    /* -------------------------------------------------------------------------------------------*/
    /* Initialize */
    init {
        obtainAttributes(attrs)
        initViews()
    }

    private fun obtainAttributes(attrs: AttributeSet?) {
        attrs ?: return

        val array = resources.obtainAttributes(attrs, R.styleable.VideoTrimmerView)
        try {
            leftBarRes =
                array.getResourceId(R.styleable.VideoTrimmerView_vtv_window_left_bar, leftBarRes)
            binding.slidingWindowView.leftBarRes = leftBarRes

            rightBarRes =
                array.getResourceId(R.styleable.VideoTrimmerView_vtv_window_right_bar, rightBarRes)
            binding.slidingWindowView.rightBarRes = rightBarRes

            barWidth =
                array.getDimension(R.styleable.VideoTrimmerView_vtv_window_bar_width, barWidth)
            binding.slidingWindowView.barWidth = barWidth

            borderWidth = array.getDimension(
                R.styleable.VideoTrimmerView_vtv_window_border_width,
                borderWidth
            )
            binding.slidingWindowView.borderWidth = borderWidth

            borderColor =
                array.getColor(R.styleable.VideoTrimmerView_vtv_window_border_color, borderColor)
            binding.slidingWindowView.borderColor = borderColor

            overlayColor =
                array.getColor(R.styleable.VideoTrimmerView_vtv_overlay_color, overlayColor)
            binding.slidingWindowView.overlayColor = overlayColor
        } finally {
            array.recycle()
        }
    }

    private fun initViews() {
        binding.videoFrameListView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val layoutParams = binding.videoFrameListView.layoutParams
        layoutParams.height = dpToPx(context, 54f).toInt()
        binding.videoFrameListView.layoutParams = layoutParams

        binding.playheadView.visibility = View.VISIBLE
        binding.playheadView.translationX = dpToPx(context, 11f)

    }

    @UnstableApi
    fun setExoPlayer(player: ExoPlayer) {
        exoPlayer = player.apply {
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (isPlaying) {
                        startUpdatingPlayhead()
                    } else {
                        stopUpdatingPlayhead()
                    }
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED) {
                        updatePlayheadPosition(
                            selectedStartTime + player.currentPosition,
                            selectedStartTime + player.duration
                        )
                    }
                }

                override fun onPositionDiscontinuity(reason: Int) {
                    updatePlayheadPosition(
                        selectedStartTime + player.currentPosition,
                        selectedStartTime + player.duration
                    )
                }
            })
        }
    }

    private fun startUpdatingPlayhead() {
        if (playheadUpdateHandler == null) {
            playheadUpdateHandler = android.os.Handler()
        }

        playheadUpdateRunnable = object : Runnable {
            override fun run() {
                exoPlayer?.let {
                    if (it.isPlaying) {
                        updatePlayheadPosition(
                            selectedStartTime + it.currentPosition,
                            selectedStartTime + it.duration
                        )
                        playheadUpdateHandler?.postDelayed(this, 16) // Update every 16ms (~60fps)
                    }
                }
            }
        }

        playheadUpdateHandler?.post(playheadUpdateRunnable!!)
    }

    private fun stopUpdatingPlayhead() {
        // 업데이트 중지 시 필요한 작업을 추가할 수 있음
        playheadUpdateHandler?.removeCallbacks(playheadUpdateRunnable!!)
    }

    // 선택된 범위 설정 메서드 (필요에 따라 호출)
    fun setSelectedRange(startMillis: Long, endMillis: Long) {
        selectedStartTime = startMillis
        selectedEndTime = endMillis
    }

    @SuppressLint("SetTextI18n")
    private fun updatePlayheadPosition(currentTime: Long, duration: Long) {
        // 현재 시간이 선택된 범위 내에 있는지 확인
//        Log.d("jung","---currentTime : " + currentTime +", selectedStartTime : " + selectedStartTime +", selectedEndTime : " + selectedEndTime)
        binding.durationView.text = CommonFunc.convertMillisToMMSS(duration)
        // 선택된 범위 내에서 현재 시간이 위치하는지 확인
        if (currentTime < selectedStartTime) {
            binding.playheadView.translationX = binding.slidingWindowView.leftBarX
            return
        } else if (currentTime > selectedEndTime) {
            binding.playheadView.translationX = binding.slidingWindowView.rightBarX
            return
        }

        val selectedRange = selectedEndTime - selectedStartTime
        if (selectedRange <= 0) return // Prevent division by zero or negative range

        val relativePosition = (currentTime - selectedStartTime).toFloat() / selectedRange
        val leftBarPosition = binding.slidingWindowView.leftBarX
        val rightBarPosition = binding.slidingWindowView.rightBarX
        val playheadPosition =
            leftBarPosition + relativePosition * (rightBarPosition - leftBarPosition)

        binding.playheadView.translationX = playheadPosition
    }

    /* -------------------------------------------------------------------------------------------*/
    /* Attach / Detach */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter = obtainVideoTrimmerPresenter()
            .apply { onViewAttached(this@VideoTrimmerView) }
        onPresenterCreated()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter?.onViewDetached()
        presenter = null
    }

    private fun onPresenterCreated() {
        presenter?.let {
            binding.slidingWindowView.listener = presenter as SlidingWindowView.Listener

            val horizontalMargin = (dpToPx(context, 11f) + barWidth).roundToInt()
            val decoration = VideoFramesDecoration(horizontalMargin, overlayColor)
            val scrollListener = VideoFramesScrollListener(
                horizontalMargin,
                presenter as VideoFramesScrollListener.Callback
            )

            binding.videoFrameListView.addItemDecoration(decoration)
            binding.videoFrameListView.addOnScrollListener(scrollListener)
        }
    }

    /* -------------------------------------------------------------------------------------------*/
    /* Public APIs */
    fun setVideo(video: File): VideoTrimmerView {
        presenter?.setVideo(video)
        return this
    }

    fun setMaxDuration(millis: Long): VideoTrimmerView {
        presenter?.setMaxDuration(millis)
        return this
    }

    fun setMinDuration(millis: Long): VideoTrimmerView {
        presenter?.setMinDuration(millis)
        return this
    }

    fun setFrameCountInWindow(count: Int): VideoTrimmerView {
        presenter?.setFrameCountInWindow(count)
        return this
    }

    fun setOnSelectedRangeChangedListener(listener: OnSelectedRangeChangedListener): VideoTrimmerView {
        presenter?.setOnSelectedRangeChangedListener(listener)
        return this
    }

    fun setExtraDragSpace(spaceInPx: Float): VideoTrimmerView {
        binding.slidingWindowView.extraDragSpace = spaceInPx
        return this
    }

    fun show() {
        presenter?.show()
    }

    /**
     * 외부저장소에 바로 저장하면 재생할수없는 영상으로 만들어져서 캐시에서 만들어서 외부저장소로 카피 -> 캐시영상은 제거
     */
    @SuppressLint("WrongConstant")
    fun saveSelectedVideoRangeToCacheFolder(
        context: Context,
        videoFile: File,
        startMillis: Long,
        endMillis: Long
    ): Pair<Boolean, String?> {
        val cacheFolder = context.cacheDir
        val outputFile = File(cacheFolder, "arpang_video_${System.currentTimeMillis()}.mp4")

        val extractor = MediaExtractor()
        val muxer: MediaMuxer
        val bufferSize: Int = 1024 * 1024 // 1MB buffer
        val buffer = ByteBuffer.allocate(bufferSize)
        val trackIndexMap = mutableMapOf<Int, Int>()
        val bufferInfo = MediaCodec.BufferInfo()

        try {
            // Extractor 설정
            extractor.setDataSource(videoFile.absolutePath)
            muxer = MediaMuxer(outputFile.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

            // 트랙 추가
            for (i in 0 until extractor.trackCount) {
                extractor.selectTrack(i)
                val format = extractor.getTrackFormat(i)
                val trackIndex = muxer.addTrack(format)
                trackIndexMap[i] = trackIndex
            }

            // 회전 정보 얻기
            val metadataRetriever = MediaMetadataRetriever()
            metadataRetriever.setDataSource(videoFile.absolutePath)
            val rotation = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)?.toInt() ?: 0
            metadataRetriever.release()
            // 회전 정보를 설정
            muxer.setOrientationHint(rotation)

            // 시작 시간으로 이동
            extractor.seekTo(startMillis * 1000, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
            muxer.start()

            // 데이터 복사
            while (true) {
                val trackIndex = extractor.sampleTrackIndex
                if (trackIndex == -1) {
                    break
                }

                val sampleTime = extractor.sampleTime
                if (sampleTime > endMillis * 1000) {
                    break
                }

                buffer.clear()
                val sampleSize = extractor.readSampleData(buffer, 0)

                if (sampleSize < 0) {
                    break
                }

                bufferInfo.offset = 0
                bufferInfo.size = sampleSize
                bufferInfo.presentationTimeUs = sampleTime
                bufferInfo.flags = extractor.sampleFlags

                val trackIndexMapped = trackIndexMap[trackIndex] ?: continue
                muxer.writeSampleData(trackIndexMapped, buffer, bufferInfo)
                extractor.advance()
            }

            muxer.stop()
            muxer.release()
            extractor.release()

            // 성공적으로 저장되었으므로, 파일 경로를 반환
            return Pair(true, outputFile.absolutePath)

        } catch (e: IOException) {
            e.printStackTrace()
            extractor.release()
            return Pair(false, null)
        }
    }


    /* -------------------------------------------------------------------------------------------*/
    /* VideoTrimmerContract.View */
    override fun getSlidingWindowWidth(): Int {
        val screenWidth = resources.displayMetrics.widthPixels
        val margin = dpToPx(context, 11f)
        return screenWidth - 2 * (margin + barWidth).roundToInt()
    }

    override fun setupAdaptor(video: File, frames: List<Long>, frameWidth: Int) {
        adaptor = VideoFramesAdaptor(video, frames, frameWidth).also {
            binding.videoFrameListView.adapter = it
        }
    }

    override fun setupSlidingWindow() {
        binding.slidingWindowView.reset()
    }

    override fun restoreSlidingWindow(left: Float, right: Float) {
        binding.slidingWindowView.setBarPositions(left, right)
    }

    override fun restoreVideoFrameList(framePosition: Int, frameOffset: Int) {
        val layoutManager =
            binding.videoFrameListView.layoutManager as? LinearLayoutManager ?: return
        layoutManager.scrollToPositionWithOffset(framePosition, frameOffset)
    }

    /* -------------------------------------------------------------------------------------------*/
    /* Listener */
    interface OnSelectedRangeChangedListener {
        fun onSelectRangeStart()
        fun onSelectRange(startMillis: Long, endMillis: Long)
        fun onSelectRangeEnd(startMillis: Long, endMillis: Long)
    }
}