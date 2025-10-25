package ca.gbc.comp3074.noveltea_app

import android.content.Context
import android.content.Context.MODE_PRIVATE

object RatingStore {
    private const val PREFS = "noveltea_prefs"
    private fun key(bookId: Int) = "rating_$bookId"

    fun set(ctx: Context, bookId: Int, value: Float) {
        ctx.getSharedPreferences(PREFS, MODE_PRIVATE)
            .edit().putFloat(key(bookId), value).apply()
    }

    fun get(ctx: Context, bookId: Int): Float =
        ctx.getSharedPreferences(PREFS, MODE_PRIVATE).getFloat(key(bookId), 0f)
}
