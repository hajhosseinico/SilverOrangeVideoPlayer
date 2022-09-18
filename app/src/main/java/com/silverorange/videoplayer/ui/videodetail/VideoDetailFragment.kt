package com.silverorange.videoplayer.ui.videodetail

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
import java.util.*


@AndroidEntryPoint
class VideoDetailFragment : Fragment() {

    private val viewModel: VideoDetailViewModel by viewModels()
    private var _binding: FragmentVideoDetailBinding? = null
    private val binding get() = _binding!!
    private var videoPlayer = HLSVideoPlayer()
    private var currentVideoIndex = 0
    private var videoList = ArrayList<VideoListNetworkEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.aspectRatioFrameLayout.setAspectRatio(16f/9f)

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
                    is DataState.Success<ArrayList<VideoListNetworkEntity>> -> {

                        if (dataState.data.isNotEmpty()) {
                            sortVideoListByDate(dataState.data)
                            videoList.addAll(dataState.data)
                            setVideoDetail(videoList[currentVideoIndex])
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
            getNextVideo()?.let { it1 -> setVideoDetail(it1) }
        }

        binding.imgPrevious.setOnClickListener {
            getPreviousVideo()?.let { it1 -> setVideoDetail(it1) }
        }

        binding.videoPlayer.setOnClickListener {
            if (binding.videoPlayerButtonHolder.visibility == View.GONE) binding.videoPlayerButtonHolder.visibility =
                View.VISIBLE
            else binding.videoPlayerButtonHolder.visibility = View.GONE
        }
    }


    private fun setDataIsEmptyOrError(errorMessage: String) {

    }

    private fun sortVideoListByDate(videoList: ArrayList<VideoListNetworkEntity>) {
        videoList.sortByDescending { it.publishedAt }
    }


    private fun loadVideo(data: VideoListNetworkEntity) {
        videoPlayer.initPlayer(requireContext(),
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

    private fun setVideoDetail(data: VideoListNetworkEntity?) {
        if (data != null) {
            resetNextAndPreviousButtonVisibility()
            binding.txtVideoTitle.text = data.title
            binding.txtVideoAuthor.text = data.author.name
            binding.txtVideoDescription.text = data.description

            loadVideo(data)
        }
    }

    private fun getNextVideo(): VideoListNetworkEntity? {

        return if (currentVideoIndex < videoList.size - 1) {
            currentVideoIndex++
            resetNextAndPreviousButtonVisibility()
            videoList[currentVideoIndex]
        } else null
    }

    private fun getPreviousVideo(): VideoListNetworkEntity? {
        return if (currentVideoIndex > 0) {
            currentVideoIndex--
            resetNextAndPreviousButtonVisibility()
            videoList[currentVideoIndex]
        } else null
    }

    private fun resetNextAndPreviousButtonVisibility() {
        binding.imgNext.visibility = View.VISIBLE
        binding.imgPrevious.visibility = View.VISIBLE

        if (currentVideoIndex == videoList.size - 1) {
            binding.imgNext.visibility = View.GONE
        }

        if (currentVideoIndex == 0) {
            binding.imgPrevious.visibility = View.GONE
        }
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