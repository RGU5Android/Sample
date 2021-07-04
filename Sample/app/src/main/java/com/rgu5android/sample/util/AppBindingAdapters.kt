package com.rgu5android.sample.util

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rgu5android.sample.R
import com.rgu5android.sample.di.module.GlideApp

object AppBindingAdapters {
    @JvmStatic
    @BindingAdapter("setImageUrl")
    fun setImageUrl(imageView: AppCompatImageView, url: String?) {
        GlideApp.with(imageView.context)
            .load(url)
            .placeholder(R.drawable.ic_outline_flag)
            .error(R.drawable.ic_outline_error)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.IMMEDIATE)
            .into(imageView)
    }
}