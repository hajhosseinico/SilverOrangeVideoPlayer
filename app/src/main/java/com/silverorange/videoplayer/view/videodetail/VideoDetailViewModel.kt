package com.silverorange.videoplayer.view.videodetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silverorange.videoplayer.model.retrofit.responsemodels.DataState
import com.silverorange.videoplayer.model.retrofit.responsemodels.VideoListNetworkEntity
import com.silverorange.videoplayer.repository.VideoRepository
import com.silverorange.videoplayer.util.HLSVideoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoDetailViewModel
@Inject
constructor(
    private val videoRepository: VideoRepository,
) : ViewModel() {

    internal var videoPlayer = HLSVideoPlayer()
    internal var videoList = ArrayList<VideoListNetworkEntity>()
    internal var currentVideoIndex = 0

    private val _dataState: MutableLiveData<DataState<ArrayList<VideoListNetworkEntity>>> =
        MutableLiveData()

    val dataState: LiveData<DataState<ArrayList<VideoListNetworkEntity>>>
        get() = _dataState

    // this methode will trigger the API on a worker thread using coroutines
    fun setStateEvent(mainStateEvent: VideoListStateEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (mainStateEvent) {
                is VideoListStateEvent.GetVideos -> {
                    videoRepository.getVideos()
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
                else -> {}
            }
        }
    }

    // getting next video is there is any
    internal fun getNextVideo(): VideoListNetworkEntity? {
        return if (currentVideoIndex < videoList.size - 1) {
            currentVideoIndex++

            videoList[currentVideoIndex]
        } else null
    }
    // getting previous video is there is any
    internal fun getPreviousVideo(): VideoListNetworkEntity? {
        return if (currentVideoIndex > 0) {
            currentVideoIndex--
            videoList[currentVideoIndex]
        } else null
    }

    // sorting videos by date
    internal fun sortVideoListByDate(videoList: ArrayList<VideoListNetworkEntity>) {
        videoList.sortByDescending { it.publishedAt }
    }
}

sealed class VideoListStateEvent {
    object GetVideos : VideoListStateEvent()
}

