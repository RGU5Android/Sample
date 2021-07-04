package com.rgu5android.sample.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rgu5android.sample.data.ApiService
import com.rgu5android.sample.data.CountryModel
import com.rgu5android.sample.data.Repository
import com.rgu5android.sample.util.CoroutineTestRule
import com.rgu5android.sample.util.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
@ExperimentalCoroutinesApi
class CountryListViewModelTest {
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
        Mockito.`when`(repository.getCountryList())
            .thenReturn(listOf(CountryModel("US", 227, "UNITED STATES", "1")))

        val viewModel = CountryListViewModel(repository)
        viewModel.countryList.observeForever {
            it?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        assertNull(resource.data)
                    }

                    Status.SUCCESS -> {
                        assertNotNull(resource.data)
                        assertEquals(1, resource.data?.size)
                    }

                    Status.ERROR -> {
                        error("Invalid flow")
                    }
                }
            }
        }
    }

    @Test
    fun `test failure api response`() = runBlockingTest {
        Mockito.`when`(repository.getCountryList()).thenThrow(RuntimeException::class.java)

        val viewModel = CountryListViewModel(repository)
        viewModel.countryList.observeForever {
            it?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        assertNull(resource.data)
                    }

                    Status.SUCCESS -> {
                        error("Invalid flow")
                    }

                    Status.ERROR -> {
                        assertNull(resource.data)
                    }
                }
            }
        }
    }
}