package com.example.donorku_app.home.imageslider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.donorku_app.databinding.ImageContainerBinding

class ImageSliderAdapter(private val items: List<ImageData>) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {
    inner class  ImageViewHolder(itemView:ImageContainerBinding) :RecyclerView.ViewHolder(itemView.root){
        private val binding = itemView
        fun bind(data:ImageData){
            with(binding){
                Glide.with(itemView)
                    .load(data.image)
                    .into(imageView)

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ImageContainerBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int =items.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(items[position])
    }
}