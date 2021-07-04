package com.rgu5android.sample.ui.list

import androidx.lifecycle.*
import com.rgu5android.sample.data.Repository
import com.rgu5android.sample.util.Resource
import javax.inject.Inject

class CountryListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val fetchValue = MutableLiveData<Boolean>()

    init {
        fetchValue.postValue(true)
    }

    val countryList = fetchValue.switchMap {
        liveData(viewModelScope.coroutineContext) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(repository.getCountryList()))
            } catch (e: Exception) {
                emit(Resource.error(data = null, message = e.message ?: "Something went wrong.!!"))
            }
        }
    }
}