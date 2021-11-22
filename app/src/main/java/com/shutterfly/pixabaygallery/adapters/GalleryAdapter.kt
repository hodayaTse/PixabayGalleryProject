package com.shutterfly.pixabaygallery.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shutterfly.pixabaygallery.R
import com.shutterfly.pixabaygallery.models.GalleryItem
import com.shutterfly.pixabaygallery.repositories.DBRepositoryImpl
import kotlinx.coroutines.runBlocking

class GalleryAdapter(private val dbRepositoryImpl: DBRepositoryImpl, private val mGalleryItem: ((GalleryItem) -> Unit)) : PagingDataAdapter<GalleryItem, GalleryViewHolder>(GalleryComparator) {

    object GalleryComparator : DiffUtil.ItemCallback<GalleryItem>() {

        override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
            return oldItem.previewUrl == newItem.previewUrl
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_image, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {

        if (position != RecyclerView.NO_POSITION) {

            getItem(position)?.let { galleryItem ->

                //load image
                Glide.with(holder.itemView)
                    .load(galleryItem.previewUrl)
                    .placeholder(ColorDrawable(Color.LTGRAY))
                    .into(holder.imageView)

                //set click image listener
                holder.itemView.setOnClickListener {
                    mGalleryItem.invoke(galleryItem)
                }

                //check if image is favorite
                runBlocking {
                       if (dbRepositoryImpl.isRowIsExist(galleryItem.id)) galleryItem.isFavorite = true
                }

                //show favorite heart image
                holder.favoriteIcon.visibility = if (galleryItem.isFavorite) View.VISIBLE else View.GONE

                //show likes counter
                holder.likes.text = galleryItem.likes

                //save image position
                galleryItem.position = position
            }
        }
    }
}

class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageView: AppCompatImageView = itemView.findViewById(R.id.image)
    val favoriteIcon: AppCompatImageView = itemView.findViewById(R.id.favorites)
    val likes: AppCompatTextView = itemView.findViewById(R.id.likes)
}