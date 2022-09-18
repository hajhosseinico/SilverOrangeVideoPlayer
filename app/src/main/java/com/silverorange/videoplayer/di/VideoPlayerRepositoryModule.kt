package com.silverorange.videoplayer.di

import com.silverorange.videoplayer.model.retrofit.VideoRetrofitInterface
import com.silverorange.videoplayer.repository.VideoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object VideoListRepositoryModule {
    @Singleton
    @Provides
    fun provideMovieListRepository(
        // providing retrofit to the view model
        retrofitInterface: VideoRetrofitInterface,
    ): VideoRepository {
        return VideoRepository(retrofitInterface)
    }
}