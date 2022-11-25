package com.example.petcare.data

sealed class BaseResult<out T : Any?>{
    data class Success<T: Any>(val data: T): BaseResult<T>()
    data class Error(val message: String): BaseResult<Nothing>()
    object Loading: BaseResult<Nothing>()
    
}
