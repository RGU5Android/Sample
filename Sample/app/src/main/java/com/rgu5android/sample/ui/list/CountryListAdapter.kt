package com.rgu5android.sample.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rgu5android.sample.R
import com.rgu5android.sample.data.CountryModel
import com.rgu5android.sample.databinding.CountryItemBinding

class CountryListAdapter(private val clickHandler: ClickHandler) :
    ListAdapter<CountryModel, CountryListAdapter.ViewHolder>(
        diffUtilComparator
    ) {
    companion object {
        private val diffUtilComparator = object : DiffUtil.ItemCallback<CountryModel>() {
            override fun areItemsTheSame(
                oldItem: CountryModel,
                newItem: CountryModel
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: CountryModel,
                newItem: CountryModel
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    class ViewHolder(private val binding: CountryItemBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(model: CountryModel, clickHandler: ClickHandler) {
            binding.model = model
            binding.root.setOnClickListener {
                clickHandler.onItemClicked(model)
            }
        }
    }

    interface ClickHandler {
        fun onItemClicked(model: CountryModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: CountryItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.country_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickHandler)
    }
}