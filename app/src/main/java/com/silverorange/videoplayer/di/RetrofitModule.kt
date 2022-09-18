package com.silverorange.videoplayer.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.silverorange.videoplayer.model.retrofit.VideoRetrofitInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Hilt modules that would be provided in view model, repository or any other where of the project that we need them
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        // we need Gson lib to convert the Api response to object
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        // Building Retrofit before providing it to the repository
        val logging = HttpLoggingInterceptor()
        // logging the response body to check if we are getting the response or modeling it correctly
        // I will remove this on release
        /**
         * todo remove this before release
         */
        logging.level = HttpLoggingInterceptor.Level.BODY

        return Retrofit.Builder()
            .baseUrl("http://192.168.2.79:4000")//192.168.2.79  //http://localhost:4000
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient.Builder().addInterceptor(logging).build())
    }

    @Singleton
    @Provides
    fun provideRetrofitInterface(retrofit: Retrofit.Builder): VideoRetrofitInterface {
        // To use retrofit in the repository, we just inject this interface to the repository
        return retrofit
            .build()
            .create(VideoRetrofitInterface::class.java)
    }

}