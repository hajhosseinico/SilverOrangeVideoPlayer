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


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return Retrofit.Builder()
            .baseUrl("http://192.168.2.79:4000")//192.168.2.79  //http://localhost:4000
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient.Builder().addInterceptor(logging).build())
    }

    @Singleton
    @Provides
    fun provideRetrofitInterface(retrofit: Retrofit.Builder): VideoRetrofitInterface {
        return retrofit
            .build()
            .create(VideoRetrofitInterface::class.java)
    }

}