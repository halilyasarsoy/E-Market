package com.halil.e_marketcase.main

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.halil.e_marketcase.data.Product
import com.halil.e_marketcase.data.local.CartDao
import com.halil.e_marketcase.data.local.ProductDao
import com.halil.e_marketcase.ui.viewmodel.ProductListViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductRepositoryTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: ProductRepository
    private lateinit var viewModel: ProductListViewModel
    private val apiService: ApiService = mockk()
    private val productDao: ProductDao = mockk(relaxed = true)
    private val cartDao: CartDao = mockk(relaxed = true)

    @Before
    fun setUp() {
        repository = ProductRepository(apiService, productDao, cartDao)
        viewModel = ProductListViewModel(repository)
    }

    @Test
    fun testGetProducts() = mainCoroutineRule.runTest {
        val apiKey = "test_api_key"
        val productList = listOf(
            Product("1", "Product 1", "image1", "100", "desc", "model", "brand", "date", false),
            Product("2", "Product 2", "image2", "200", "desc", "model", "brand", "date", false)
        )

        coEvery { apiService.getProducts(apiKey) } returns productList

        val result = repository.getProducts(apiKey)

        assertEquals(productList, result)
        coVerify { apiService.getProducts(apiKey) }
    }

    @Test
    fun testGetAllProductsFromDb() {
        val productList = listOf(
            Product("1", "Product 1", "image1", "100", "desc", "model", "brand", "date", false),
            Product("2", "Product 2", "image2", "200", "desc", "model", "brand", "date", false)
        )

        val liveData = MutableLiveData<List<Product>>()
        liveData.postValue(productList)

        every { productDao.getAllProducts() } returns liveData

        val result = repository.allProducts

        assertEquals(productList, result.value)
        verify { productDao.getAllProducts() }
    }
}