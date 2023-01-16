package com.silverorange.videoplayer

import com.google.gson.annotations.SerializedName

data class VideoDataModel(
    // on below line creating variables for our modal class
    // make sure that variable name should be same to
    // that of key which is used in json response.
    @SerializedName("id")
    var id: String,

    @SerializedName("title")
    var title: String,

    @SerializedName("hlsURL")
    var hlsURL: String,

    @SerializedName("fullURL")
    var fullURL: String,

    @SerializedName("description")
    var description: String,


    @SerializedName("author")
    var author: Author,

)

data class Author (
    val id : String,
    val name : String
)