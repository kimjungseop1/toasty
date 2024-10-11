import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.RowTemplateBinding

class BottomIconAdapter(
    private val context: Context
) : RecyclerView.Adapter<BottomIconAdapter.ViewHolder>() {
    private var mIconListener: IconListener? = null
    private var selectedPosition = RecyclerView.NO_POSITION

    fun setIconListener(iconListener: IconListener?) {
        mIconListener = iconListener
    }

    interface IconListener {
        fun onIconClick(bitmap: Bitmap)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomIconAdapter.ViewHolder {
        val binding = RowTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BottomIconAdapter.ViewHolder, position: Int) {
        // Load image into the ImageView
        Glide.with(context)
            .asBitmap()
            .load(R.drawable.sample_img_1)
            .into(holder.binding.imgTemp)

    }

    override fun getItemCount(): Int {
        return 30
    }

    inner class ViewHolder(val binding: RowTemplateBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val adapterPosition = layoutPosition

                // Update selected position
                if (adapterPosition != RecyclerView.NO_POSITION) {
//                    notifyItemChanged(selectedPosition)
                    selectedPosition = adapterPosition
                    notifyItemChanged(selectedPosition)

                    // Trigger listener callback
                    mIconListener?.let { listener ->
                        Glide.with(itemView.context)
                            .asBitmap()
                            .load(R.drawable.sample_img_1)
                            .into(object : CustomTarget<Bitmap?>(256, 256) {
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                                    listener.onIconClick(resource)
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {}
                            })
                    }
                }
            }
        }
    }
}
