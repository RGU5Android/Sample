package com.rgu5android.sample.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.rgu5android.sample.R
import com.rgu5android.sample.data.CountryModel
import com.rgu5android.sample.databinding.CountryListFragmentBinding
import com.rgu5android.sample.util.Resource
import com.rgu5android.sample.util.Status
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CountryListFragment : DaggerFragment() {

    @Inject
    internal lateinit var viewModelProviderFactory: ViewModelProvider.Factory

    lateinit var binding: CountryListFragmentBinding

    private val viewModel: CountryListViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(CountryListViewModel::class.java)
    }

    private val adapter: CountryListAdapter by lazy {
        CountryListAdapter(object : CountryListAdapter.ClickHandler {
            override fun onItemClicked(model: CountryModel) {
                binding.provinceNavController?.let { fragmentContainerView ->
                    fragmentContainerView.findNavController().navigate(
                        R.id.country_province_list_fragment,
                        Bundle().apply {
                            putInt("countryId", model.id)
                            putString("countryName", model.name)
                        })
                } ?: kotlin.run {
                    findNavController().navigate(
                        CountryListFragmentDirections.actionCountryListFragmentToCountryProvinceListFragment(
                            model.id, model.name
                        )
                    )
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.country_list_fragment, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUi()

        viewModel.countryList.observe(viewLifecycleOwner, countryListObserver)
    }

    private fun setUpUi() {
        binding.buttonRetry.setOnClickListener {
            viewModel.fetchValue.postValue(true)
        }

        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = this@CountryListFragment.adapter
        }
    }

    private val countryListObserver = Observer<Resource<List<CountryModel>>> {
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