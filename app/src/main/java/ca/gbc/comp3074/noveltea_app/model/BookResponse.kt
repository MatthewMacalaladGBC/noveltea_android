package ca.gbc.comp3074.noveltea_app.model

// For retrieving a list of book data to populate screen after search
// Temporary fields for now - will update as needed
data class BookResponse(
    val docs: List<BookDoc>
)

data class BookDoc(
    val title: String?
)

