package dev.tim9h.rcpandroid.ui.utils;

import android.util.TypedValue;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.core.content.ContextCompat;

public class BindingUtils {

    private BindingUtils() {
        // Utility class
    }

    public static void setDynamicTextColor(TextView textView, @AttrRes int attrId) {
        var typedValue = new TypedValue();
        textView.getContext().getTheme().resolveAttribute(attrId, typedValue, true);
        var color = ContextCompat.getColor(textView.getContext(), typedValue.resourceId);
        textView.setTextColor(color);
    }

}
