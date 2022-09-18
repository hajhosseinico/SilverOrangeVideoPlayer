package com.silverorange.videoplayer.model.retrofit.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// I use Model at the end of this class name because this is a model that is used in some part of the application.
data class VideoListAuthorModel(
    @SerializedName("id")
    @Expose
    var id: String,

    @SerializedName("name")
    @Expose
    var name: String,
)