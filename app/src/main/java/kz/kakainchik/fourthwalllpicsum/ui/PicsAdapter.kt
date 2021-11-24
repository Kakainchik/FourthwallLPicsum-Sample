package kz.kakainchik.fourthwalllpicsum.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.kakainchik.fourthwalllpicsum.R
import kz.kakainchik.fourthwalllpicsum.entities.Picture
import kz.kakainchik.fourthwalllpicsum.models.HOSTNAME
import javax.inject.Inject

/**
 * RecyclerView adapter implementing the list of [Picture]. Supports pagination.
 */
class PicsAdapter @Inject constructor(private val onItemClick: (Picture) -> Unit) :
    PagingDataAdapter<Picture, PicsAdapter.ViewHolder>(PictureDiffCallback) {

    class ViewHolder(view: View, onItemClick: (Picture) -> Unit) : RecyclerView.ViewHolder(view) {
        //Thought about storing this url as a format string but Glide has own implementation
        private val smallImageUrlPath = "$HOSTNAME/200?image="

        private val image: ImageView = view.findViewById(R.id.pic_item_image)

        private var currentPicture: Picture? = null

        init {
            view.setOnClickListener {
                currentPicture?.let {
                    onItemClick(it)
                }
            }
        }

        fun bind(pic: Picture?) {
            currentPicture = pic?.also {
                Glide.with(image)
                    .load(smallImageUrlPath + pic.id)
                    .placeholder(R.drawable.ic_sharp_image_24)
                    .into(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pic_list_item, parent, false)
        return ViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

object PictureDiffCallback : DiffUtil.ItemCallback<Picture>() {
    override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean {
        return oldItem == newItem
    }
}