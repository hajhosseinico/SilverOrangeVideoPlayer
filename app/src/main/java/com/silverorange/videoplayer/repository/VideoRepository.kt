package com.silverorange.videoplayer.repository

import com.silverorange.videoplayer.model.responsemodels.DataState
import com.silverorange.videoplayer.model.retrofit.VideoRetrofitInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VideoRepository
constructor(
    private val videoRetrofitInterface: VideoRetrofitInterface,
) {
    suspend fun getVideos(
        workOffline: Boolean
    ): Flow<DataState<List<String>>> =
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
