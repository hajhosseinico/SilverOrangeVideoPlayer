package com.silverorange.videoplayer.ui.videodetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silverorange.videoplayer.model.retrofit.responsemodels.DataState
import com.silverorange.videoplayer.model.retrofit.responsemodels.VideoListNetworkEntity
import com.silverorange.videoplayer.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val _dataState: MutableLiveData<DataState<List<VideoListNetworkEntity>>> =
        MutableLiveData()

    fun setStateEvent(mainStateEvent: VideoListStateEvent) {
        viewModelScope.launch() {
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
}

sealed class VideoListStateEvent {
    object GetVideos : VideoListStateEvent()
}

