package com.silverorange.videoplayer.ui.videodetail

import android.os.Bundle
import android.util.Log
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
import com.silverorange.videoplayer.util.HLSVideoPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class VideoDetailFragment : Fragment() {

    private val viewModel: VideoDetailViewModel by viewModels()
    private var _binding: FragmentVideoDetailBinding? = null
    private val binding get() = _binding!!
    private var videoPlayer = HLSVideoPlayer()
    private var currentVideoIndex = 0
    private lateinit var videoList: List<VideoListNetworkEntity>

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
        setVideoPlayerClickListeners()
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
                            videoList = dataState.data
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

    private fun setVideoPlayerClickListeners() {
        binding.imgPlayPause.setOnClickListener {
            if (videoPlayer.isPlaying()) videoPlayer.onPause() else videoPlayer.onPlayOrResume()
        }

        binding.imgNext.setOnClickListener {
            getNextVideo()?.let { it1 -> loadVideo(it1) }
        }

        binding.imgPrevious.setOnClickListener {
            getPreviousVideo()?.let { it1 -> loadVideo(it1) }
        }
    }

    private fun setDataIsEmptyOrError(errorMessage: String) {

    }

    private fun loadVideo(data: VideoListNetworkEntity) {
        videoPlayer.initPlayer(requireContext(),
            data.hlsURL,
            binding.videoPlayer,
            object : HLSVideoPlayer.VideoPlayerCallbacks {
                override fun onInitialized() {
                    Log.i("LOG", "player is initialized")
                }

                override fun onPlaying() {
                    Log.i("LOG", "player is playing")
                }

                override fun onPause() {
                    Log.i("LOG", "player is paused")
                }

                override fun onStop() {
                    Log.i("LOG", "player is stopped")
                }
            })
    }

    private fun setVideoDetail(data: VideoListNetworkEntity) {
        binding.txtVideoTitle.text = data.title
        binding.txtVideoAuthor.text = data.author.name
        binding.txtVideoDescription.text = data.description

        loadVideo(data)
    }

    private fun getCurrentVideoPosition(data: List<VideoListNetworkEntity>): VideoListNetworkEntity {
        return data[currentVideoIndex]
    }

    private fun getNextVideo(): VideoListNetworkEntity? {
        return null
    }

    private fun getPreviousVideo(): VideoListNetworkEntity? {
        return null
    }

    override fun onPause() {
        super.onPause()
        videoPlayer.onPause()
    }

    override fun onStop() {
        super.onStop()
        videoPlayer.onStop()
    }
}