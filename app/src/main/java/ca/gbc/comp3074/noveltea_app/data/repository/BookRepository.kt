package ca.gbc.comp3074.noveltea_app.data.repository

import ca.gbc.comp3074.noveltea_app.data.api.OpenLibraryApi
import ca.gbc.comp3074.noveltea_app.data.api.RetrofitClient
import ca.gbc.comp3074.noveltea_app.model.Book

// Repository to map pulled details to the model
class BookRepository {

    private val api = RetrofitClient.instance.create(OpenLibraryApi::class.java)

    suspend fun getBooksByQuery(query: String): List<Book> {
        val response = api.searchBooks(query)
        return response.docs.mapNotNull { doc ->
            val id = doc.key?.removePrefix("/works/")
            val title = doc.title ?: "Untitled"
            val author = doc.author_name?.firstOrNull() ?: "Unknown"
            val cover = doc.cover_i.let { "https://covers.openlibrary.org/b/id/$it-L.jpg" }

            if (id != null)
                Book(
                    id = id,
                    title = title,
                    author = author,
                    description = null, // Don't need description when populating search
                    coverImgUrl = cover
                )
            else null
        }
    }

    suspend fun getTrendingBooks(limit: Int = 20): List<Book> {
        val topics = listOf("fiction", "fantasy", "romance", "history", "mystery", "science")
        val randomTopic = topics.random()
        val response = api.searchBooks(randomTopic)

        // Map results and limit count
        return response.docs
            .take(limit)
            .mapNotNull { doc ->
                val id = doc.key?.removePrefix("/works/")
                val author = doc.author_name?.firstOrNull() ?: "Unknown"
                val coverURL = doc.cover_i?.let { "https://covers.openlibrary.org/b/id/$it-L.jpg" }

                if (id != null)
                    Book(
                        id = id,
                        title = doc.title ?: "Untitled",
                        author = author,
                        description = null,
                        coverImgUrl = coverURL
                    )
                else null
            }
    }

    suspend fun getBookDetails(workId: String): Book {
        val response = api.getBookDetails(workId)

        val title = response.title ?: "Title could not be found"

        val description = when (response.description) {
            is String -> response.description
            is Map<*, *> -> response.description["value"] as? String
            else -> "No description could be found."
        }

        val coverURL = response.covers?.firstOrNull()?.let { id ->
            "https://covers.openlibrary.org/b/id/$id-L.jpg"
        } ?: ""

        val authorKey = response.authors?.firstOrNull()?.author?.key?.removePrefix("/authors/")
        val authorName = if (!authorKey.isNullOrEmpty()) {
            try {
                val authorResponse = api.getAuthorDetails(authorKey)
                authorResponse.name ?: ""
            } catch (e: Exception) {
                "Unable to fetch name"
            }
        } else {
            "Unable to fetch name"
        }

        return Book(
            id = workId,
            title = title,
            author = authorName,
            description = description,
            coverImgUrl = coverURL
        )

    }

}