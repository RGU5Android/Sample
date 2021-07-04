package com.rgu5android.sample.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rgu5android.sample.util.CoroutineTestRule
import com.rgu5android.sample.util.MockResponseFileReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class RepositoryTest {
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Sets the main coroutines dispatcher to a TestCoroutineScope for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private lateinit var mockWebServer: MockWebServer

    private val moshi: Moshi by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .baseUrl(mockWebServer.url("/"))
            .build()
    }

    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    private val repository: Repository by lazy {
        Repository(apiService)
    }

    private val countryList: List<CountryModel> by lazy {
        listOf(
            CountryModel("US", 227, "UNITED STATES", "1"),
            CountryModel("IN", 104, "INDIA", "91")
        )
    }

    private val provinceList: List<ProvinceModel> by lazy {
        listOf(
            ProvinceModel("AL", "US", 97, "Alabama"),
            ProvinceModel("AK", "US", 101, "Alaska"),
            ProvinceModel("AS", "US", 132, "American Samoa")
        )
    }


    private val dispatcher: Dispatcher = object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/rest/worldregions/country" -> {
                    MockResponse().setResponseCode(HttpURLConnection.HTTP_OK)
                        .setBody(MockResponseFileReader("CountryList.json").content)
                }
                "/rest/worldregions/country/227/province" -> {
                    MockResponse().setResponseCode(HttpURLConnection.HTTP_OK)
                        .setBody(MockResponseFileReader("ProvinceListUS.json").content)
                }
                "/rest/worldregions/country/17/province" -> {
                    MockResponse().setResponseCode(HttpURLConnection.HTTP_OK)
                        .setBody(MockResponseFileReader("ProvinceListAX.json").content)
                }
                else -> {
                    MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                        .setBody("API not found.!!")
                }
            }
        }
    }

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        mockWebServer.dispatcher = dispatcher
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `country list`() = runBlocking {
        assertEquals(countryList, repository.getCountryList())

    }

    @Test
    fun `province list for non-empty provinces country`() = runBlocking {
        assertEquals(provinceList, repository.getProvinceForCountry(227))
    }

    @Test
    fun `province list for empty provinces country`() = runBlocking {
        assertEquals(listOf<ProvinceModel>(), repository.getProvinceForCountry(17))
    }
}