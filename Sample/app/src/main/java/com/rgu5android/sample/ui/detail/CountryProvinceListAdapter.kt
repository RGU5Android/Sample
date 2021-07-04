package com.rgu5android.sample.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rgu5android.sample.R
import com.rgu5android.sample.data.ProvinceModel
import com.rgu5android.sample.databinding.ProvinceItemBinding

class CountryProvinceListAdapter :
    ListAdapter<ProvinceModel, CountryProvinceListAdapter.ViewHolder>(diffUtilComparator) {

    companion object {
        private val diffUtilComparator = object : DiffUtil.ItemCallback<ProvinceModel>() {
            override fun areItemsTheSame(
                oldItem: ProvinceModel,
                newItem: ProvinceModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ProvinceModel,
                newItem: ProvinceModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(private val binding: ProvinceItemBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(model: ProvinceModel) {
            binding.model = model
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ProvinceItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.province_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}