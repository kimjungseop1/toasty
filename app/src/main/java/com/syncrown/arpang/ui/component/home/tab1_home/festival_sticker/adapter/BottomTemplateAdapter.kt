import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.RowTemplateBinding

class BottomTemplateAdapter(
    private val context: Context
) : RecyclerView.Adapter<BottomTemplateAdapter.ViewHolder>() {
    private var mTempListener: TemplateListener? = null
    private var selectedPosition = RecyclerView.NO_POSITION

    fun setTemplateListener(templateListener: TemplateListener?) {
        mTempListener = templateListener
    }

    interface TemplateListener {
        fun onTemplateClick(bitmap: Bitmap)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomTemplateAdapter.ViewHolder {
        val binding = RowTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BottomTemplateAdapter.ViewHolder, position: Int) {
        // Load image into the ImageView
        Glide.with(context)
            .asBitmap()
            .load(R.drawable.sample_img_1)
            .into(holder.binding.imgTemp)

        // Highlight the selected item with a yellow border
        if (selectedPosition == position) {
            holder.binding.imgTemp.foreground = ContextCompat.getDrawable(context, R.drawable.selected_border)
        } else {
            holder.binding.imgTemp.foreground = null
        }
    }

    override fun getItemCount(): Int {
        return 20
    }

    inner class ViewHolder(val binding: RowTemplateBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val adapterPosition = layoutPosition

                // Update selected position
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedPosition) // Remove border from previously selected item
                    selectedPosition = adapterPosition
                    notifyItemChanged(selectedPosition) // Add border to the newly selected item

                    // Trigger listener callback
                    mTempListener?.let { listener ->
                        Glide.with(itemView.context)
                            .asBitmap()
                            .load(R.drawable.sample_img_1)
                            .into(object : CustomTarget<Bitmap?>(256, 256) {
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                                    listener.onTemplateClick(resource)
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {}
                            })
                    }
                }
            }
        }
    }
}
