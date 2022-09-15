package com.silverorange.videoplayer.model.retrofit

import retrofit2.http.GET

/**
 * Api queries
 * used by retrofit
 */
interface VideoRetrofitInterface {
    @GET("address")
    suspend fun getVideoList(): List<String>
}