package ca.gbc.comp3074.noveltea_app.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

object RatingStore {
    private const val PREFS = "noveltea_prefs"
    private fun key(bookId: String) = "rating_$bookId"

    fun set(ctx: Context, bookId: String, value: Float) {
        ctx.getSharedPreferences(PREFS, MODE_PRIVATE)
            .edit { putFloat(key(bookId), value) }
    }

    fun get(ctx: Context, bookId: String): Float =
        ctx.getSharedPreferences(PREFS, MODE_PRIVATE).getFloat(key(bookId), 0f)
}