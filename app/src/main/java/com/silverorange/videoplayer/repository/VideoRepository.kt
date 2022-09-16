package com.silverorange.videoplayer.repository

import com.silverorange.videoplayer.model.retrofit.responsemodels.DataState
import com.silverorange.videoplayer.model.retrofit.VideoRetrofitInterface
import com.silverorange.videoplayer.model.retrofit.responsemodels.VideoListNetworkEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VideoRepository
constructor(
    private val videoRetrofitInterface: VideoRetrofitInterface,
) {
    suspend fun getVideos(
    ): Flow<DataState<List<VideoListNetworkEntity>>> =
        flow {
            emit(DataState.Loading)

                try {
                    val response = videoRetrofitInterface.getVideoList()
                    emit(DataState.Success(response))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
        }
}
