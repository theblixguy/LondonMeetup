package com.suyashsrijan.londonmeetup.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.suyashsrijan.londonmeetup.R;

public class BitmapUtils {

    public static BitmapDescriptor getMarkerIconFromDrawable(Context context, int id) {
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), id, null);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        bitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, false);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static Drawable getDrawableForTubeLine(Context context, String lineName) {
        if (lineName.equalsIgnoreCase("Bakerloo")) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.bakerloo_line_logo, null);
        } else if (lineName.equalsIgnoreCase("Central")) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.central_line_logo, null);
        } else if (lineName.equalsIgnoreCase("Circle")) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.circle_line_logo, null);
        } else if (lineName.equalsIgnoreCase("District")) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.district_line_logo, null);
        } else if (lineName.equalsIgnoreCase("Hammersmith & City")) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.hammersmith_line_logo, null);
        } else if (lineName.equalsIgnoreCase("Jubilee")) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.jubilee_line_logo, null);
        } else if (lineName.equalsIgnoreCase("Metropolitan")) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.metropolitan_line_logo, null);
        } else if (lineName.equalsIgnoreCase("Northern")) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.northern_line_logo, null);
        } else if (lineName.equalsIgnoreCase("Piccadilly")) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.piccadilly_line_logo, null);
        } else if (lineName.equalsIgnoreCase("Victoria")) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.victoria_line_logo, null);
        } else if (lineName.equalsIgnoreCase("Waterloo & City")) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.waterloo_line_logo, null);
        } else return null;
    }

    public static Bitmap getBitmapForBusLine(Context context, String busLine) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bus_number_overlay);
        Bitmap overlay = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(50.f);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawText(busLine, (bitmap.getWidth() / 2.f) , (bitmap.getHeight() / 2.f) + 16.f, paint);
        return overlay;
    }

}
