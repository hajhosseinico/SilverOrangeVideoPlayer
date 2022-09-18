package com.silverorange.videoplayer.model.retrofit.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// I use NetworkEntity because this class is an API response and it will directly be used in the application ny API/APIs
data class VideoListNetworkEntity(
    @SerializedName("id") @Expose var id: String,

    @SerializedName("title") @Expose var title: String,

    @SerializedName("hlsURL") @Expose var hlsURL: String,

    @SerializedName("fullURL") @Expose var fullURL: String,

    @SerializedName("description") @Expose var description: String,

    @SerializedName("publishedAt") @Expose var publishedAt: String,

    @SerializedName("author") @Expose var author: VideoListAuthorModel,
)