package com.halil.e_marketcase.ui.viewmodel

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.halil.e_marketcase.data.Product
import com.halil.e_marketcase.main.ProductRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ProductListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: ProductRepository
    private lateinit var viewModel: ProductListViewModel

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        viewModel = ProductListViewModel(repository)
    }

    @Test
    fun getAllProducts() = runTest {
        val productList = listOf(
            Product("1", "Product 1", "image1", "100", "desc", "model", "brand", "date", false),
            Product("2", "Product 2", "image2", "200", "desc", "model", "brand", "date", false)
        )

        val liveData = MutableLiveData<List<Product>>()
        liveData.postValue(productList)

        every { repository.allProducts } returns liveData

        // Observe LiveData
        viewModel.allProducts.observeForever { }

        // Wait for LiveData to update
        advanceUntilIdle()

        // Check that the value of allProducts is as expected
        assertEquals(productList, viewModel.allProducts.value)
    }
}