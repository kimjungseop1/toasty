package com.syncrown.toasty.ui.component.home.tab1_home.ar_print.videotrimmer.tools

import android.animation.Animator
import android.animation.AnimatorSet
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.ref.WeakReference


internal class SetVideoThumbnailAsyncTask @JvmOverloads constructor(
    view: ImageView,
    private val timeMs: Long = 0L,
    private val size: Int = 512,
    private val fadeDuration: Long = 0L
) {

    private val viewRef = WeakReference(view)

    fun setThumbnail(file: File) {
        // Start the coroutine on the Main thread
        CoroutineScope(Dispatchers.Main).launch {
            val bitmap = withContext(Dispatchers.IO) {
                // Fetch the bitmap in the background
                retrieveThumbnail(file)
            }
            // Update the UI on the Main thread
            onPostExecute(bitmap)
        }
    }

    private fun retrieveThumbnail(file: File): Bitmap? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(file.path)
            val timeUs = if (timeMs == 0L) -1 else timeMs * 1000
            val bitmap = retriever.getFrameAtTime(timeUs)
            bitmap?.let { scaleBitmap(it, size) }
        } catch (e: Exception) {
            null
        } finally {
            runCatching { retriever.release() }
        }
    }

    private fun onPostExecute(result: Bitmap?) {
        val view = viewRef.get() ?: return

        result?.let {
            if (fadeDuration == 0L) {
                view.setImageBitmap(it)
                return
            }

            val fadeOut = animateAlpha(
                view,
                1f,
                0f,
                fadeDuration,
                autoPlay = false,
                listener = fadeOutEndListener(view, result)
            )
            val fadeIn = animateAlpha(view, 0f, 1f, fadeDuration, autoPlay = false)

            val animators = AnimatorSet()
            animators.playSequentially(fadeOut, fadeIn)
            animators.start()
        }
    }

    private fun fadeOutEndListener(view: ImageView, result: Bitmap): Animator.AnimatorListener {
        return object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {}
            override fun onAnimationEnd(p0: Animator) {
                view.setImageBitmap(result)
            }

            override fun onAnimationCancel(p0: Animator) {}
            override fun onAnimationRepeat(p0: Animator) {}
        }
    }

    private fun scaleBitmap(bitmap: Bitmap, size: Int): Bitmap {
        // Implement the scaling logic here
        return Bitmap.createScaledBitmap(bitmap, size, size, false)
    }
}
