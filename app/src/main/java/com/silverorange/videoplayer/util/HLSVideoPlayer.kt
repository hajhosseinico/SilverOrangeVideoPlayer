package com.silverorange.videoplayer.util

import android.content.Context
import android.util.Log
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.Listener
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

    fun initPlayer(
        context: Context, url: String, pView: PlayerView, _callbacks: VideoPlayerCallbacks
    ) {
        releasePlayer()
        playerView = pView
        callbacks = _callbacks
        dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "testapp"))

        val mediaItem =
            MediaItem.Builder().setUri(url).setMimeType(MimeTypes.APPLICATION_M3U8).build()

        exoPlayer = SimpleExoPlayer.Builder(context).build().apply {
            playWhenReady = isPlayerPlaying
            seekTo(currentWindow, playbackPosition)
            playWhenReady = false
            setMediaItem(mediaItem, false)
            prepare()
        }
        playerView.player = exoPlayer

        exoPlayer.addListener(object : Player.Listener { // player listener

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) { // check player play back state
                    Player.STATE_READY -> {
                        Log.i("LOG_Player", "STATE_READY")
                        if (exoPlayer.isPlaying)
                            callbacks.onPlaying()
                        else
                            callbacks.onPause()
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
            currentWindow = exoPlayer.currentWindowIndex
            exoPlayer.release()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun onPlayOrResume() {
        playerView.player?.play()
        callbacks.onPlaying()
    }

    fun isPlaying(): Boolean {
        return playerView.player?.isPlaying == true
    }

    fun onPause() {
        playerView.player?.pause()
        callbacks.onPause()

    }

    fun onStop() {
        playerView.player?.stop()
        releasePlayer()
        callbacks.onPause()
    }

    interface VideoPlayerCallbacks {
        fun onInitialized()
        fun onPlaying()
        fun onPause()
        fun onStop()
    }


}