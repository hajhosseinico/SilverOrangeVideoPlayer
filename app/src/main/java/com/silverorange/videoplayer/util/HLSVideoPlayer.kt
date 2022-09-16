package com.silverorange.videoplayer.util

import android.content.Context
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util


class HLSVideoPlayer() {
    private lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var dataSourceFactory: DataSource.Factory
    private lateinit var playerView: PlayerView

    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var isFullscreen = false
    private var isPlayerPlaying = true
    private lateinit var callbacks: VideoPlayerCallbacks

    public fun initPlayer(
        context: Context, url: String, pView: PlayerView, _callbacks: VideoPlayerCallbacks
    ) {
        playerView = pView
        callbacks = _callbacks
        dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "testapp"))

        val mediaItem =
            MediaItem.Builder().setUri(url).setMimeType(MimeTypes.APPLICATION_M3U8).build()

        exoPlayer = SimpleExoPlayer.Builder(context).build().apply {
            playWhenReady = isPlayerPlaying
            seekTo(currentWindow, playbackPosition)
            setMediaItem(mediaItem, false)
            prepare()
        }
        playerView.player = exoPlayer
        callbacks.onInitialized()
    }

    private fun releasePlayer() {
        isPlayerPlaying = exoPlayer.playWhenReady
        playbackPosition = exoPlayer.currentPosition
        currentWindow = exoPlayer.currentWindowIndex
        exoPlayer.release()
    }

    fun onPlayOrResume() {
        if (Util.SDK_INT <= 23) {
            playerView.onResume()
            callbacks.onPlaying()
        }
    }

    fun isPlaying(): Boolean {
        return playerView.player?.isPlaying == true
    }

    public fun onPause() {
        if (Util.SDK_INT <= 23) {
            playerView.onPause()
            releasePlayer()
            callbacks.onPause()

        }
    }

    public fun onStop() {
        if (Util.SDK_INT > 23) {
            playerView.onPause()
            releasePlayer()
            callbacks.onPause()

        }
    }

    interface VideoPlayerCallbacks {
        fun onInitialized()
        fun onPlaying()
        fun onPause()
        fun onStop()
    }


}