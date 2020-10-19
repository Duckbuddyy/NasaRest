package com.duckbuddyy.nasarest.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.duckbuddyy.nasarest.R
import com.duckbuddyy.nasarest.data.Photo
import com.duckbuddyy.nasarest.databinding.ItemNasaPhotoBinding

class NasaPhotoAdapter (private val listener:OnItemClickListener):
    PagingDataAdapter<Photo, NasaPhotoAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {

    inner class PhotoViewHolder(private val binding: ItemNasaPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if(position != RecyclerView.NO_POSITION){
                    val item = getItem(position)
                    if(item !=null){
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(photo: Photo){
            binding.apply {
                Glide.with(itemView)
                    .load(photo.imgSrc)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    //.apply(RequestOptions().transform(RoundedCorners(100)))
                    .into(imageView)

                textView.text =photo.camera.fullName
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(photo:Photo)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position)

        if(currentItem != null){
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding =
            ItemNasaPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PhotoViewHolder(binding)
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem == newItem

        }
    }
}