package ca.gbc.comp3074.noveltea_app.model

// For retrieving a list of book data to populate screen after search
// Uses search.json Open Library API endpoint
data class BookResponse(
    val docs: List<BookDoc>
)

data class BookDoc(
    val key: String?,
    val title: String?,
    val author: List<String>?,
    val coverId: Int?
)

