package com.silverorange.videoplayer

import com.silverorange.videoplayer.model.retrofit.responsemodels.DataState
import com.silverorange.videoplayer.model.retrofit.responsemodels.VideoListNetworkEntity
import com.silverorange.videoplayer.repository.VideoRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import javax.inject.Inject

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class VideoDetailTest {

    // testing the API using Hilt and Robolectric
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    // injecting Hilt
    @Before // using this for initializing the objects or etc, such as Authenticating.
    fun setUp() {
        hiltRule.inject()
    }

    // Injecting Repository to call the API
    @Inject
    lateinit var videoRepository: VideoRepository

    @Test
    fun `requestGetVideos api response is successful`() {
        runBlocking {
            val response = videoRepository.getVideos()

            // response.last() because the first emits the Loading in my repository code (emit(DataState.Loading) and the last would be Error or Success
            genericAssertResponse(response.last()){
                Assert.assertTrue(it.data.isNotEmpty())
            }

        }
    }

    private suspend fun <T> genericAssertResponse(
        response: DataState<T>,
        successFun: suspend (response: DataState.Success<T>) -> Unit
    ) {
        when (response) {
            is DataState.Success -> {
                successFun(response)
            }
            else -> {
                assert(false)
            }
        }
    }

}