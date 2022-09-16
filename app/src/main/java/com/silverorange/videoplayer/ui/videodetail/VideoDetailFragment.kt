package com.silverorange.videoplayer.ui.videodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.silverorange.videoplayer.R
import com.silverorange.videoplayer.databinding.FragmentVideoDetailBinding
import com.silverorange.videoplayer.model.retrofit.responsemodels.DataState
import com.silverorange.videoplayer.model.retrofit.responsemodels.VideoListNetworkEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class VideoDetailFragment : Fragment() {

    private val viewModel: VideoDetailViewModel by viewModels()
    private var _binding: FragmentVideoDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        getVideoList()
    }

    private fun getVideoList() {
        viewModel.setStateEvent(VideoListStateEvent.GetVideos)
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            CoroutineScope(Dispatchers.Main).launch {
                when (dataState) {
                    is DataState.Success<List<VideoListNetworkEntity>> -> {

                        if (dataState.data.isNotEmpty()) {
                            setVideoDetail(getCurrentVideoPosition(dataState.data))
                        } else {
                            setDataIsEmptyOrError(getString(R.string.empty_response))
                        }
                    }
                    is DataState.Error -> {
                        setDataIsEmptyOrError(dataState.exception.message.toString())
                    }
                    else -> {}
                }
            }
        })
    }

    private fun setDataIsEmptyOrError(errorMessage: String) {

    }

    private fun loadVideo(data : VideoListNetworkEntity) {

    }

    private fun setVideoDetail(data: VideoListNetworkEntity) {
        binding.txtVideoTitle.text = data.title
        binding.txtVideoAuthor.text = data.author.name
        binding.txtVideoDescription.text = data.description
    }

    private fun getCurrentVideoPosition(data: List<VideoListNetworkEntity>): VideoListNetworkEntity {
        // this methode should validate video count, find out if this is the first or the last video
        // and also check if there is any next video available
        return data[0]
    }


}