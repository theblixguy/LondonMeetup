package com.suyashsrijan.londonmeetup.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.suyashsrijan.londonmeetup.R

object BitmapUtils {

    fun getMarkerIconFromDrawable(context: Context, id: Int): BitmapDescriptor {
        val drawable = ResourcesCompat.getDrawable(context.resources, id, null)
        val canvas = Canvas()
        var bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        bitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, false)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun getDrawableForTubeLine(context: Context, lineName: String): Drawable? {
        return if (lineName.equals("Bakerloo", ignoreCase = true)) {
            ResourcesCompat.getDrawable(context.resources, R.drawable.bakerloo_line_logo, null)
        } else if (lineName.equals("Central", ignoreCase = true)) {
            ResourcesCompat.getDrawable(context.resources, R.drawable.central_line_logo, null)
        } else if (lineName.equals("Circle", ignoreCase = true)) {
            ResourcesCompat.getDrawable(context.resources, R.drawable.circle_line_logo, null)
        } else if (lineName.equals("District", ignoreCase = true)) {
            ResourcesCompat.getDrawable(context.resources, R.drawable.district_line_logo, null)
        } else if (lineName.equals("Hammersmith & City", ignoreCase = true)) {
            ResourcesCompat.getDrawable(context.resources, R.drawable.hammersmith_line_logo, null)
        } else if (lineName.equals("Jubilee", ignoreCase = true)) {
            ResourcesCompat.getDrawable(context.resources, R.drawable.jubilee_line_logo, null)
        } else if (lineName.equals("Metropolitan", ignoreCase = true)) {
            ResourcesCompat.getDrawable(context.resources, R.drawable.metropolitan_line_logo, null)
        } else if (lineName.equals("Northern", ignoreCase = true)) {
            ResourcesCompat.getDrawable(context.resources, R.drawable.northern_line_logo, null)
        } else if (lineName.equals("Piccadilly", ignoreCase = true)) {
            ResourcesCompat.getDrawable(context.resources, R.drawable.piccadilly_line_logo, null)
        } else if (lineName.equals("Victoria", ignoreCase = true)) {
            ResourcesCompat.getDrawable(context.resources, R.drawable.victoria_line_logo, null)
        } else if (lineName.equals("Waterloo & City", ignoreCase = true)) {
            ResourcesCompat.getDrawable(context.resources, R.drawable.waterloo_line_logo, null)
        } else
            null
    }

    fun getBitmapForBusLine(context: Context, busLine: String): Bitmap {
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.bus_number_overlay)
        val overlay = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(overlay)
        val paint = Paint()
        paint.color = Color.WHITE
        paint.isAntiAlias = true
        paint.textSize = 50f
        paint.textAlign = Paint.Align.CENTER
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        canvas.drawText(busLine, bitmap.width / 2f, bitmap.height / 2f + 16f, paint)
        return overlay
    }

}
