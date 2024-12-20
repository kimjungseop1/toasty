package com.syncrown.arpang.network

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : NetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)
    class NetCode<T>(msg: String) : NetworkResult<T>(message = msg)
}
