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

    // I use MVVM Architecture so this is my repository.
    // If I had API caching, I would have used Cache class interfaces here too (you can see Api caching samples in my GitHub projects)
    suspend fun getVideos(
    ): Flow<DataState<ArrayList<VideoListNetworkEntity>>> =
        flow {
            emit(DataState.Loading)

            try {
                val response = videoRetrofitInterface.getVideoList()
                emit(DataState.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(DataState.Error(e))
            }
        }
}
