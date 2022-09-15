package com.silverorange.videoplayer.model

import com.silverorange.videoplayer.model.retrofit.responsemodels.VideoListNetworkEntity
import retrofit2.http.GET

interface VideoDetailRetrofitInterface {
    @GET("/videos")
    suspend fun getVideoList(): ArrayList<VideoListNetworkEntity>
}