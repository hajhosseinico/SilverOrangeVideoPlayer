package com.silverorange.videoplayer.model.retrofit

import com.silverorange.videoplayer.model.retrofit.responsemodels.VideoListNetworkEntity
import retrofit2.http.GET

/**
 * Api queries
 * used by retrofit
 */
interface VideoRetrofitInterface {
    @GET("address")
    suspend fun getVideoList(): List<VideoListNetworkEntity>
}