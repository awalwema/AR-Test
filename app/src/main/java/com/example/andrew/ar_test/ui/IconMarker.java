package com.example.andrew.ar_test.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.andrew.ar_test.ui.objects.PaintableIcon;

/**
 * This class extends Marker and draws an icon instead of a circle for it's
 * visual representation.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class IconMarker extends Marker {

    private Bitmap bitmap = null;

    public IconMarker(String name, double latitude, double longitude, double altitude, int color,
                      int type, Bitmap bitmap) {
        super(name, latitude, longitude, altitude, color, type);
        this.bitmap = bitmap;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void drawIcon(Canvas canvas) {
        if (canvas == null || bitmap == null) throw new NullPointerException();

        // gpsSymbol is defined in Marker
        if (gpsSymbol == null) gpsSymbol = new PaintableIcon(bitmap, 96, 96);
        super.drawIcon(canvas);
    }
}
