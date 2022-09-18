package com.silverorange.videoplayer.view.videodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.caverock.androidsvg.SVG
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
class VideoDetailFragment() : Fragment() {

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

        // setting video view aspect ratio
        binding.aspectRatioFrameLayout.setAspectRatio(16f / 9f)

        subscribeObservers()
        getVideoList()
        setVideoPlayerClickListeners()
    }

    private fun getVideoList() {
        // triggering the API from View -> ViewModel -> Repository and then get the response from Live data observers
        viewModel.setStateEvent(VideoListStateEvent.GetVideos)
    }

    private fun subscribeObservers() {
        // getting the API response and showing it in the MainTread using coroutines, live data and retrofit
        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            CoroutineScope(Dispatchers.Main).launch {
                when (dataState) {
                    is DataState.Success<ArrayList<VideoListNetworkEntity>> -> {

                        if (dataState.data.isNotEmpty()) {
                            viewModel.sortVideoListByDate(dataState.data)
                            viewModel.videoList.addAll(dataState.data)
                            setVideoDetail(viewModel.videoList[viewModel.currentVideoIndex])
                        } else {
                            showError(getString(R.string.empty_response))
                        }
                    }
                    is DataState.Error -> {
                        showError(dataState.exception.message.toString())
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
    }

    private fun setVideoPlayerClickListeners() {
        binding.imgPlayPause.setOnClickListener {
            if (viewModel.videoPlayer.isPlaying()) viewModel.videoPlayer.onPause() else viewModel.videoPlayer.onPlayOrResume()
        }

        binding.imgNext.setOnClickListener {
            viewModel.getNextVideo()?.let { video -> setVideoDetail(video) }
            resetNextAndPreviousButtonVisibility()
        }

        binding.imgPrevious.setOnClickListener {
            viewModel.getPreviousVideo()?.let { video -> setVideoDetail(video) }
            resetNextAndPreviousButtonVisibility()
        }

        binding.videoPlayer.setOnClickListener {
            if (binding.videoPlayerButtonHolder.visibility == View.GONE) binding.videoPlayerButtonHolder.visibility =
                View.VISIBLE
            else binding.videoPlayerButtonHolder.visibility = View.GONE
        }
    }

    // initialize the video player and getting the states of the player
    private fun loadVideo(data: VideoListNetworkEntity) {
        viewModel.videoPlayer.initPlayer(requireContext(),
            data.hlsURL,
            binding.videoPlayer,
            object : HLSVideoPlayer.VideoPlayerCallbacks {
                override fun onInitialized() {
                    binding.videoPlayerButtonHolder.visibility = View.VISIBLE
                }

                override fun onPlaying() {
                    val svg = SVG.getFromAsset(context!!.assets, "pause.svg")
                    binding.imgPlayPause.setSVG(svg)
                    binding.videoPlayerButtonHolder.visibility = View.GONE
                }

                override fun onPause() {
                    val svg = SVG.getFromAsset(context!!.assets, "play.svg")
                    binding.imgPlayPause.setSVG(svg)
                    binding.videoPlayerButtonHolder.visibility = View.VISIBLE
                }

                override fun onStop() {
                    binding.videoPlayerButtonHolder.visibility = View.GONE
                }
            })
    }

    // setting video player detail including texts and video
    private fun setVideoDetail(data: VideoListNetworkEntity?) {
        if (data != null) {
            resetNextAndPreviousButtonVisibility()
            binding.txtVideoTitle.text = data.title
            binding.txtVideoAuthor.text = data.author.name
            binding.txtVideoDescription.text = data.description

            loadVideo(data)
        }
    }

    private fun resetNextAndPreviousButtonVisibility() {
        binding.imgNext.visibility = View.VISIBLE
        binding.imgPrevious.visibility = View.VISIBLE

        if (viewModel.currentVideoIndex == viewModel.videoList.size - 1) {
            binding.imgNext.visibility = View.GONE
        }

        if (viewModel.currentVideoIndex == 0) {
            binding.imgPrevious.visibility = View.GONE
        }
    }

    // pausing the video when fragment pauses
    override fun onPause() {
        super.onPause()
        viewModel.videoPlayer.onPause()
    }

    // releasing the memory to prevent MEMORY LEAKS!
    override fun onStop() {
        super.onStop()
        viewModel.videoPlayer.onStop()
    }
}