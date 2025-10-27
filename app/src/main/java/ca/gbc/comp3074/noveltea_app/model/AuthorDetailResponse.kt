package ca.gbc.comp3074.noveltea_app.model

import com.google.gson.annotations.SerializedName

data class AuthorDetailResponse(
    @SerializedName("name")
    val name: String?
)
