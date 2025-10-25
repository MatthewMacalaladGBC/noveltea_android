package ca.gbc.comp3074.noveltea_app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val Amber = Color(0xFFFFC107)
private val Gray  = Color(0xFFBDBDBD)

@Composable
fun StarRating(
    value: Float,
    onChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    max: Int = 5
) {
    Row(modifier) {
        for (i in 1..max) {
            val idx = i.toFloat()
            val icon = when {
                value >= idx           -> Icons.Filled.Star
                value >= idx - 0.5f    -> Icons.Filled.StarHalf
                else                   -> Icons.Outlined.Star
            }
            val tint = if (value >= idx - 0.5f) Amber else Gray

            Icon(
                imageVector = icon,
                contentDescription = "Rate ${idx} stars",
                tint = tint,
                modifier = Modifier
                    .size(size)
                    .clickable {
                        // tap cycles .5 -> full -> previous
                        val next = when {
                            value < idx - 0.5f -> idx - 0.5f
                            value < idx        -> idx
                            else               -> idx - 1f
                        }.coerceIn(0f, max.toFloat())
                        onChange(kotlin.math.round(next * 2f) / 2f)
                    }
            )
        }
    }
}

@Composable
fun StarDisplay(
    value: Float,
    size: Dp = 16.dp,
    max: Int = 5
) {
    Row {
        for (i in 1..max) {
            val idx = i.toFloat()
            val icon = when {
                value >= idx           -> Icons.Filled.Star
                value >= idx - 0.5f    -> Icons.Filled.StarHalf
                else                   -> Icons.Outlined.Star
            }
            val tint = if (value >= idx - 0.5f) Amber else Gray
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(size))
        }
    }
}
