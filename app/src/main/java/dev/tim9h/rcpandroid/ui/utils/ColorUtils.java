package dev.tim9h.rcpandroid.ui.utils;


import android.content.Context;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;

import dev.tim9h.rcpandroid.R;

public class ColorUtils {
    private ColorUtils() {
        // Utility class
    }

    public static int getDynamicColor(Context ctx, int attribute) {
        var tv = new TypedValue();
        if (ctx != null) {
            ctx.getTheme().resolveAttribute(attribute, tv, true);
            return ContextCompat.getColor(ctx, tv.resourceId);
        } else {
            return R.color.teal_700;
        }
    }

    public static int getPrimary(Context ctx) {
        return getDynamicColor(ctx, com.google.android.material.R.attr.colorPrimary);
    }

    public static int getSurfaceVariant(Context ctx) {
        return getDynamicColor(ctx, com.google.android.material.R.attr.colorSurfaceVariant);
    }

    public static int getColorOnSurface(Context ctx) {
        return getDynamicColor(ctx, com.google.android.material.R.attr.colorOnSurface);
    }

}
