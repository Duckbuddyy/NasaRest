package com.duckbuddyy.nasarest.data


import com.google.gson.annotations.SerializedName

data class Photo(
    val camera: Camera,
    @SerializedName("earth_date")
    val earthDate: String,
    val id: Int,
    @SerializedName("img_src")
    private val _imgSrc: String,
    val rover: Rover,
    val sol: Int
) {
    //Image Source Url needs replacement with HTTPS to communicate with app
    val imgSrc: String
        get() = if (_imgSrc.indexOf("https://") != -1) _imgSrc else _imgSrc.replace(
            "http://",
            "https://"
        )
}