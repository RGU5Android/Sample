package com.rgu5android.sample.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.rgu5android.sample.R
import com.rgu5android.sample.data.ProvinceModel
import com.rgu5android.sample.databinding.CountryProvinceListFragmentBinding
import com.rgu5android.sample.util.Resource
import com.rgu5android.sample.util.Status
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CountryProvinceListFragment : DaggerFragment() {
    @Inject
    internal lateinit var viewModelProviderFactory: ViewModelProvider.Factory

    lateinit var binding: CountryProvinceListFragmentBinding

    private val viewModel: CountryProvinceListViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelProviderFactory
        ).get(CountryProvinceListViewModel::class.java)
    }

    private val adapter: CountryProvinceListAdapter by lazy {
        CountryProvinceListAdapter()
    }

    val safeArgs: CountryProvinceListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.country_province_list_fragment,
                container,
                false
            )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUi()

        viewModel.provinceList.observe(viewLifecycleOwner, provinceListObserver)

        if (safeArgs.countryId != 0) {
            viewModel.setCountryId(safeArgs.countryId)
        }
    }

    private fun setUpUi() {
        binding.buttonRetry.setOnClickListener {
            viewModel.retry()
        }

        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = this@CountryProvinceListFragment.adapter
        }

        if (safeArgs.countryId == 0) {
            populateViews(View.GONE, View.GONE, View.GONE, View.GONE)
        }

        if (safeArgs.countryName.isBlank()) {
            binding.textViewCountryName.visibility = View.GONE
        } else {
            binding.textViewCountryName.visibility = View.VISIBLE
            binding.textViewCountryName.text = safeArgs.countryName
        }
    }

    private val provinceListObserver = Observer<Resource<List<ProvinceModel>>> {
        it?.let { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    populateViews(View.GONE, View.GONE, View.VISIBLE)
                    adapter.submitList(null)
                }

                Status.SUCCESS -> {
                    if (resource.data.isNullOrEmpty()) {
                        populateViews(View.GONE, View.GONE, View.GONE, View.VISIBLE)
                        adapter.submitList(null)
                    } else {
                        populateViews(View.VISIBLE, View.GONE, View.GONE)
                        adapter.submitList(resource.data)
                    }
                }

                Status.ERROR -> {
                    populateViews(View.GONE, View.VISIBLE, View.GONE)
                    binding.textViewErrorMessage.text = resource.message
                    adapter.submitList(null)
                }
            }
        }
    }

    private fun populateViews(
        shouldShowRecyclerView: Int,
        shouldShowErrorView: Int,
        shouldShowShimmerView: Int,
        shouldShowEmptyView: Int = View.GONE
    ) {
        binding.recyclerView.visibility = shouldShowRecyclerView
        binding.groupErrorView.visibility = shouldShowErrorView
        binding.shimmerView.visibility = shouldShowShimmerView
        binding.textViewEmpty.visibility = shouldShowEmptyView
    }

}