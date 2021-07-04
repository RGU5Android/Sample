package com.rgu5android.sample.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rgu5android.sample.data.ApiService
import com.rgu5android.sample.data.ProvinceModel
import com.rgu5android.sample.data.Repository
import com.rgu5android.sample.util.CoroutineTestRule
import com.rgu5android.sample.util.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
@ExperimentalCoroutinesApi
class CountryProvinceListViewModelTest {
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Mock
    lateinit var apiService: ApiService

    @InjectMocks
    lateinit var repository: Repository

    @Test
    fun `test success api response`() = runBlockingTest {
        val countryId = 227
        var tryRetry = true

        Mockito.`when`(repository.getProvinceForCountry(countryId))
            .thenReturn(
                listOf(
                    ProvinceModel("AL", "US", 97, "Alabama"),
                    ProvinceModel("AK", "US", 101, "Alaska"),
                    ProvinceModel("AS", "US", 132, "American Samoa")
                )
            )


        val viewModel = CountryProvinceListViewModel(repository)

        viewModel.provinceList.observeForever {
            it?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        Assert.assertNull(resource.data)
                    }

                    Status.SUCCESS -> {
                        Assert.assertNotNull(resource.data)
                        Assert.assertEquals(3, resource.data?.size)
                        if (tryRetry) {
                            tryRetry = false
                            viewModel.retry()
                        }
                    }

                    Status.ERROR -> {
                        error("Invalid flow")
                    }
                }
            }
        }

        viewModel.setCountryId(countryId)
    }

    @Test
    fun `test failure api response`() = runBlockingTest {
        val countryId = 227
        var tryRetry = true

        Mockito.`when`(repository.getProvinceForCountry(countryId))
            .thenThrow(RuntimeException::class.java)

        val viewModel = CountryProvinceListViewModel(repository)

        viewModel.provinceList.observeForever {
            it?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        Assert.assertNull(resource.data)
                    }

                    Status.SUCCESS -> {
                        error("Invalid flow")
                    }

                    Status.ERROR -> {
                        Assert.assertNull(resource.data)
                        if (tryRetry) {
                            tryRetry = false
                            viewModel.retry()
                        }
                    }
                }
            }
        }

        viewModel.setCountryId(countryId)
    }
}