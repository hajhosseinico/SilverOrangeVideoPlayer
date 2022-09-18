package com.silverorange.videoplayer.util

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.Listener
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.MimeTypes

// Simple custom ExoPlayer
class HLSVideoPlayer() {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var playerView: StyledPlayerView

    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var isPlayerPlaying = true
    private lateinit var callbacks: VideoPlayerCallbacks

    fun initPlayer(
        context: Context, url: String, pView: StyledPlayerView, _callbacks: VideoPlayerCallbacks
    ) {
        releasePlayer()
        playerView = pView
        callbacks = _callbacks

        val mediaItem =
            MediaItem.Builder().setUri(url).setMimeType(MimeTypes.APPLICATION_M3U8).build()

        exoPlayer = ExoPlayer.Builder(context).build().apply {
            seekTo(currentWindow, playbackPosition)
            playWhenReady = false
            val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
            val mediaSource =
                HlsMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem)
            setMediaSource(mediaSource)
            setMediaItem(mediaItem, false)
            prepare()
        }

        playerView.player = exoPlayer

        exoPlayer.addListener(object : Listener {
            override fun onPlaybackStateChanged(@Player.State state: Int) {
                when (state) { // check player play back state
                    Player.STATE_READY -> {
                        Log.i("LOG_Player", "STATE_READY")
                        if (exoPlayer.isPlaying) callbacks.onPlaying()
                        else callbacks.onPause()
                    }
                    Player.STATE_ENDED -> {
                        callbacks.onStop()
                    }
                    else -> {}
                }
            }
        })

        callbacks.onInitialized()
    }

    private fun releasePlayer() {
        try {
            isPlayerPlaying = exoPlayer.playWhenReady
            playbackPosition = exoPlayer.currentPosition
            currentWindow = exoPlayer.currentMediaItemIndex
            exoPlayer.release()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun onPlayOrResume() {
        try {
            playerView.player?.play()
            callbacks.onPlaying()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isPlaying(): Boolean {
        return try {
            playerView.player?.isPlaying == true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun onPause() {
        try {
            playerView.player?.pause()
            callbacks.onPause()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun onStop() {
        try {
            playerView.player?.stop()
            releasePlayer()
            callbacks.onPause()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface VideoPlayerCallbacks {
        fun onInitialized()
        fun onPlaying()
        fun onPause()
        fun onStop()
    }
}