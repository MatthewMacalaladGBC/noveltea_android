package ca.gbc.comp3074.noveltea_app.data.api

import ca.gbc.comp3074.noveltea_app.model.BookDetailResponse
import ca.gbc.comp3074.noveltea_app.model.BookResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryApi {

    // Search for books by keyword (will return a list of books that match the search queries via the API)
    @GET("search.json")
    suspend fun searchBooks(@Query("q") query: String): BookResponse

    // Get detailed information for a specific book (returns relevant info for a specific work pulled from the API)
    @GET("works/{workId}.json")
    suspend fun getBookDetails(@Path("workId") workId: String): BookDetailResponse

}