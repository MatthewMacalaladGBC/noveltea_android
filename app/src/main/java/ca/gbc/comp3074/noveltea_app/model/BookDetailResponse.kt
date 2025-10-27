package ca.gbc.comp3074.noveltea_app.model

// For retrieving detailed info for a single book
// Temporary fields for now - will update as needed
data class BookDetailResponse(
    val title: String?,
    val authors: List<AuthorRef>?,
    val description: Any?, // In case description is mapped out rather than just the value (i.e. description: type, value)
    val coverId: List<Int>?
)

data class AuthorRef(
    val author: AuthorKey?
)

data class AuthorKey(
    val key: String?
)