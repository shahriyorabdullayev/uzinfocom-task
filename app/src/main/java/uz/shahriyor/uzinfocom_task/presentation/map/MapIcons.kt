package uz.shahriyor.uzinfocom_task.presentation.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toBitmap
import com.yandex.runtime.image.ImageProvider
import uz.shahriyor.uzinfocom_task.R

object MapIcons {

    private val cache = HashMap<String, ImageProvider>()

    fun poi(context: Context, color: Int): ImageProvider = cached("poi_$color") {
        val density = context.resources.displayMetrics.density
        val size = (26 * density).toInt()
        val radius = size / 2f
        val bitmap = createBitmap(size, size)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE
        canvas.drawCircle(radius, radius, radius, paint)
        paint.color = color
        canvas.drawCircle(radius, radius, radius - 3 * density, paint)
        bitmap
    }

    fun cluster(context: Context, count: Int, color: Int): ImageProvider = cached("cluster_${count}_$color") {
        val density = context.resources.displayMetrics.density
        val text = count.toString()
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = Color.WHITE
            textSize = 13 * density
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }
        val size = (maxOf(36 * density, textPaint.measureText(text) + 20 * density)).toInt()
        val radius = size / 2f
        val bitmap = createBitmap(size, size)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE
        canvas.drawCircle(radius, radius, radius, paint)
        paint.color = color
        canvas.drawCircle(radius, radius, radius - 3 * density, paint)
        val baseline = radius - (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(text, radius, baseline, textPaint)
        bitmap
    }

    fun car(context: Context): ImageProvider = cached("car") {
        val density = context.resources.displayMetrics.density
        val drawable = requireNotNull(ContextCompat.getDrawable(context, R.drawable.ic_car))
        drawable.toBitmap((48 * density).toInt(), (48 * density).toInt())
    }

    private fun cached(id: String, create: () -> Bitmap): ImageProvider =
        cache.getOrPut(id) { ImageProvider.fromBitmap(create(), true, id) }
}
