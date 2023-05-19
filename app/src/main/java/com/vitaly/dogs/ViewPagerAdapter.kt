package com.vitaly.dogs

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.json.JSONArray

class ViewPagerAdapter(private val context: Context, private var images: JSONArray): RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_pager_item, parent, false))
    }

    override fun getItemCount(): Int {
        return images.length()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(images[position].toString()).into(holder.imageView)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newImages: JSONArray) {
        images = newImages
        notifyDataSetChanged()
    }

}