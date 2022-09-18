package com.silverorange.videoplayer.model.retrofit

import com.silverorange.videoplayer.model.retrofit.responsemodels.VideoListNetworkEntity
import retrofit2.http.GET

interface VideoRetrofitInterface {
    @GET("/videos")
    suspend fun getVideoList(): ArrayList<VideoListNetworkEntity>
}