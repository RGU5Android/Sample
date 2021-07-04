package com.rgu5android.sample.ui.detail

import androidx.lifecycle.*
import com.rgu5android.sample.data.Repository
import com.rgu5android.sample.util.Resource
import javax.inject.Inject

class CountryProvinceListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _countryId = MutableLiveData<Int>()

    val provinceList = _countryId.switchMap { countryId ->
        liveData(viewModelScope.coroutineContext) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(repository.getProvinceForCountry(countryId)))
            } catch (e: Exception) {
                emit(Resource.error(data = null, message = e.message ?: "Something went wrong.!!"))
            }
        }
    }

    fun setCountryId(countryId: Int) {
        if (_countryId.value == null) {
            _countryId.postValue(countryId)
        }
    }

    fun retry() {
        _countryId.postValue(_countryId.value)
    }
}