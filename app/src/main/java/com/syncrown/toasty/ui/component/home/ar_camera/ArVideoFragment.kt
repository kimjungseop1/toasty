package com.syncrown.toasty.ui.component.home.ar_camera

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.RectF
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT
import android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION
import android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.animation.doOnStart
import androidx.core.graphics.rotationMatrix
import androidx.core.graphics.transform
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.ar.core.AugmentedImage
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.rendering.ExternalTexture
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.syncrown.toasty.R
import com.syncrown.toasty.ui.component.home.ar_camera.node.VideoAnchorNode
import com.syncrown.toasty.ui.component.home.ar_camera.node.VideoScaleType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException

class ArVideoFragment : ArFragment() {
    private lateinit var mediaPlayer: ExoPlayer
    private lateinit var externalTexture: ExternalTexture
    private lateinit var videoRenderable: ModelRenderable
    private lateinit var videoAnchorNode: VideoAnchorNode

    private var activeAugmentedImage: AugmentedImage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = ExoPlayer.Builder(requireContext()).build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)

        // 평면인식하는 하얀점들 안보이도록
        arSceneView.planeRenderer.isEnabled = false
        arSceneView.isLightEstimationEnabled = false

        initializeSession()
        createArScene()

        return view
    }

    override fun getSessionConfiguration(session: Session): Config {

        suspend fun loadAugmentedImageBitmapFromUrl(imageUrl: String): Bitmap? {
            return withContext(Dispatchers.IO) {
                try {
                    val futureTarget = Glide.with(requireContext())
                        .asBitmap()
                        .load(imageUrl)
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    futureTarget.get()
                } catch (e: Exception) {
                    Log.e(TAG, "URL로부터 비트맵 로드 안됨", e)
                    null
                }
            }
        }

        fun setupAugmentedImageDatabase(config: Config, session: Session): Boolean = runBlocking {
            try {
                val augmentedImageDatabase = AugmentedImageDatabase(session)
                val imageUrls =
                    listOf(TEST_IMAGE_1_URL, TEST_IMAGE_2_URL, TEST_IMAGE_3_URL, TEST_IMAGE_4_URL)
                val videoUrls =
                    listOf(TEST_VIDEO_1_URL, TEST_VIDEO_2_URL, TEST_VIDEO_3_URL, TEST_VIDEO_4_URL)

                for (i in imageUrls.indices) {
                    val bitmap = loadAugmentedImageBitmapFromUrl(imageUrls[i])
                    if (bitmap != null) {
                        augmentedImageDatabase.addImage(videoUrls[i], bitmap)
                    }
                }

                config.augmentedImageDatabase = augmentedImageDatabase
                true
            } catch (e: IllegalArgumentException) {
                Log.e(TAG, "비트맵을 증강 이미지 데이터베이스에 추가할 수 없습니다.", e)
                false
            } catch (e: IOException) {
                Log.e(TAG, "증강 이미지 비트맵을 로드하는 중 IO 예외가 발생했습니다.", e)
                false
            }
        }

        return super.getSessionConfiguration(session).also {
            it.lightEstimationMode = Config.LightEstimationMode.DISABLED
            it.focusMode = Config.FocusMode.AUTO

            if (!setupAugmentedImageDatabase(it, session)) {
                Toast.makeText(
                    requireContext(),
                    "비트맵을 증강 이미지 데이터베이스에 추가할 수 없습니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun createArScene() {
        externalTexture = ExternalTexture().also {
            mediaPlayer.setVideoSurface(it.surface)
        }

        ModelRenderable.builder()
            .setSource(requireContext(), R.raw.augmented_video_model)
            .build()
            .thenAccept { renderable ->
                videoRenderable = renderable
                renderable.isShadowCaster = false
                renderable.isShadowReceiver = false
                renderable.material.setExternalTexture("videoTexture", externalTexture)
            }
            .exceptionally { throwable ->
                Log.e(TAG, "Could not create ModelRenderable", throwable)
                return@exceptionally null
            }

        videoAnchorNode = VideoAnchorNode().apply {
            setParent(arSceneView.scene)
        }
    }

    override fun onUpdate(frameTime: FrameTime) {
        val frame = arSceneView.arFrame ?: return

        val updatedAugmentedImages = frame.getUpdatedTrackables(AugmentedImage::class.java)

        val nonFullTrackingImages =
            updatedAugmentedImages.filter { it.trackingMethod != AugmentedImage.TrackingMethod.FULL_TRACKING }
        activeAugmentedImage?.let { activeAugmentedImage ->
            if (isArVideoPlaying() && nonFullTrackingImages.any {
                    it.index == activeAugmentedImage.index
                }) {
                pauseArVideo()
            }
        }

        val fullTrackingImages =
            updatedAugmentedImages.filter { it.trackingMethod == AugmentedImage.TrackingMethod.FULL_TRACKING }
        if (fullTrackingImages.isEmpty()) {
            return
        }

        activeAugmentedImage?.let { activeAugmentedImage ->
            if (fullTrackingImages.any {
                    it.index == activeAugmentedImage.index
                }) {
                if (!isArVideoPlaying()) {
                    resumeArVideo()
                }
                return
            }
        }

        fullTrackingImages.firstOrNull()?.let { augmentedImage ->
            try {
                playbackArVideo(augmentedImage.name, augmentedImage)
            } catch (e: Exception) {
                Log.e(TAG, "Could not play video [${augmentedImage.name}]", e)
            }
        }
    }

    private fun isArVideoPlaying() = mediaPlayer.isPlaying

    private fun pauseArVideo() {
        videoAnchorNode.renderable = null
        mediaPlayer.pause()
    }

    private fun resumeArVideo() {
        mediaPlayer.play()
        fadeInVideo()
    }

    private fun dismissArVideo() {
        videoAnchorNode.anchor?.detach()
        videoAnchorNode.renderable = null
        activeAugmentedImage = null
        mediaPlayer.release()
        mediaPlayer.clearMediaItems()
    }

    private fun playbackArVideo(videoUrl: String, augmentedImage: AugmentedImage) {
        Log.d(TAG, "playbackVideo = $videoUrl")

        lifecycleScope.launch {
            try {
                val metadataRetriever = MediaMetadataRetriever()

                metadataRetriever.setDataSource(videoUrl)

                val videoWidth =
                    metadataRetriever.extractMetadata(METADATA_KEY_VIDEO_WIDTH)?.toFloatOrNull()
                        ?: 0f
                val videoHeight =
                    metadataRetriever.extractMetadata(METADATA_KEY_VIDEO_HEIGHT)?.toFloatOrNull()
                        ?: 0f
                val videoRotation =
                    metadataRetriever.extractMetadata(METADATA_KEY_VIDEO_ROTATION)?.toFloatOrNull()
                        ?: 0f

                val imageSize = RectF(0f, 0f, augmentedImage.extentX, augmentedImage.extentZ)
                    .transform(rotationMatrix(videoRotation))

                val videoScaleType = VideoScaleType.CenterCrop

                videoAnchorNode.setVideoProperties(
                    videoWidth = videoWidth,
                    videoHeight = videoHeight,
                    videoRotation = videoRotation,
                    imageWidth = imageSize.width(),
                    imageHeight = imageSize.height(),
                    videoScaleType = videoScaleType
                )

                videoRenderable.material.apply {
                    setFloat2(MATERIAL_IMAGE_SIZE, imageSize.width(), imageSize.height())
                    setFloat2(MATERIAL_VIDEO_SIZE, videoWidth, videoHeight)
                    setBoolean(
                        MATERIAL_VIDEO_CROP,
                        VIDEO_CROP_ENABLED
                    )
                }

                mediaPlayer.apply {
                    setMediaItem(MediaItem.fromUri(videoUrl))
                    repeatMode = Player.REPEAT_MODE_ONE // 반복재생
                    playWhenReady = true // 준비 완료 후 재생 시작
                    prepare()
                }

                videoAnchorNode.anchor?.detach()
                videoAnchorNode.anchor = augmentedImage.createAnchor(augmentedImage.centerPose)

                activeAugmentedImage = augmentedImage

                externalTexture.surfaceTexture.setOnFrameAvailableListener {
                    it.setOnFrameAvailableListener(null)
                    fadeInVideo()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error retrieving video metadata", e)
            }
        }

    }

    private fun fadeInVideo() {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 400L
            interpolator = LinearInterpolator()
            addUpdateListener { v ->
                videoRenderable.material.setFloat(MATERIAL_VIDEO_ALPHA, v.animatedValue as Float)
            }
            doOnStart { videoAnchorNode.renderable = videoRenderable }
            start()
        }
    }

    override fun onPause() {
        super.onPause()
        dismissArVideo()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    companion object {
        private const val TAG = "jung"

        private const val TEST_IMAGE_1_URL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg"
        private const val TEST_IMAGE_2_URL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg"
        private const val TEST_IMAGE_3_URL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg"
        private const val TEST_IMAGE_4_URL =
            "https://media.istockphoto.com/id/607913622/ko/%EC%82%AC%EC%A7%84/%EC%83%81%EC%A7%95%EC%A0%81%EC%9D%B8-%EB%B8%8C%EB%A3%A8%ED%81%B4%EB%A6%B0-%EB%8B%A4%EB%A6%AC.jpg?s=2048x2048&w=is&k=20&c=rZHAR8T4z2MgewsN04AYB7KcSJJ7bwserIjCRbyDzjA="

        private const val TEST_VIDEO_1_URL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        private const val TEST_VIDEO_2_URL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
        private const val TEST_VIDEO_3_URL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
        private const val TEST_VIDEO_4_URL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4"

        private const val VIDEO_CROP_ENABLED = true

        private const val MATERIAL_IMAGE_SIZE = "imageSize"
        private const val MATERIAL_VIDEO_SIZE = "videoSize"
        private const val MATERIAL_VIDEO_CROP = "videoCropEnabled"
        private const val MATERIAL_VIDEO_ALPHA = "videoAlpha"
    }
}