package ca.gbc.comp3074.noveltea_app.model

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val description: String?,
    val coverImgUrl: String?
)