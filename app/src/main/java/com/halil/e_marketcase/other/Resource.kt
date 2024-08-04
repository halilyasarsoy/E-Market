package com.halil.e_marketcase.other

sealed class Resource<T>(val data: T? = null, var message: String? = null) {
    class Success<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Completed<T>() : Resource<T>()
}