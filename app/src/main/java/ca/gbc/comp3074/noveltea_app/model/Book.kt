package ca.gbc.comp3074.noveltea_app.model

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val rating: Double,
    val description: String,
    val coverImgUrl: String
)