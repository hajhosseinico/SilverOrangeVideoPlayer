package com.silverorange.videoplayer.model.retrofit

import retrofit2.http.GET
import java.util.*
import kotlin.collections.ArrayList

/**
 * Api queries
 * used by retrofit
 */
interface VideoRetrofitInterface {
    @GET("address")
    suspend fun getVideoList(): ArrayList<String>
}