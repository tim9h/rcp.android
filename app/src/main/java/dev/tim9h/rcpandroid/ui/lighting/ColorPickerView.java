package dev.tim9h.rcpandroid.ui.lighting;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A custom view that displays a circular color picker.
 * <p>
 * This view allows the user to select a color by touching a point on a color wheel.
 * It handles Hue and Saturation selection, while Brightness (Value) can be controlled externally.
 */
public class ColorPickerView extends View {

    public interface OnColorChangedListener {
        void onColorChanged(int color);
    }

    private OnColorChangedListener onColorChangedListener;

    private final float[] hsv = new float[]{0f, 0f, 1f}; // Hue, Saturation, Value

    private final Paint colorWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint selectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float selectorRadius = 0f;
    private float centerX = 0f;
    private float centerY = 0f;
    private float radius = 0f;
    private float selectorX = 0f;
    private float selectorY = 0f;

    public ColorPickerView(Context context) {
        super(context);
        init();
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        selectorPaint.setColor(Color.BLACK);
        selectorPaint.setStyle(Paint.Style.STROKE);
        selectorPaint.setStrokeWidth(4f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2f;
        centerY = h / 2f;
        radius = Math.min(centerX, centerY) * 0.8f; // Use 80% of the available space
        selectorRadius = radius * 0.05f;

        // Initial position of the selector (center)
        selectorX = centerX;
        selectorY = centerY;

        // Create bitmap shaders here because they depend on the view's size
        var hueShader = new SweepGradient(centerX, centerY,
                new int[]{Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED}, null);
        var saturationShader = new RadialGradient(centerX, centerY, radius, Color.WHITE, 0x00FFFFFF, Shader.TileMode.CLAMP);
        colorWheelPaint.setShader(new ComposeShader(hueShader, saturationShader, PorterDuff.Mode.SRC_OVER));
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // Draw the color wheel
        canvas.drawCircle(centerX, centerY, radius, colorWheelPaint);

        // Draw the selector circle
        canvas.drawCircle(selectorX, selectorY, selectorRadius, selectorPaint);
        var innerSelectorPaint = new Paint(selectorPaint);
        innerSelectorPaint.setColor(Color.WHITE);
        innerSelectorPaint.setStrokeWidth(2f);
        canvas.drawCircle(selectorX, selectorY, selectorRadius - 2f, innerSelectorPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                var x = event.getX();
                var y = event.getY();

                var dx = x - centerX;
                var dy = y - centerY;
                var distance = (float) Math.sqrt(dx * dx + dy * dy);

                // Clamp to the bounds of the circle
                if (distance <= radius) {
                    selectorX = x;
                    selectorY = y;
                } else {
                    selectorX = centerX + (dx / distance) * radius;
                    selectorY = centerY + (dy / distance) * radius;
                }

                updateColorFromPosition();
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void updateColorFromPosition() {
        var dx = selectorX - centerX;
        var dy = selectorY - centerY;
        var distance = (float) Math.sqrt(dx * dx + dy * dy);

        // Update Saturation based on distance from the center
        hsv[1] = Math.max(0f, Math.min(1f, distance / radius));

        // Update Hue based on the angle
        if (distance > 0) {
            var angle = (float) Math.toDegrees(Math.atan2(-dy, dx));
            if (angle < 0) {
                angle += 360f;
            }
            hsv[0] = angle;
        } else {
            hsv[0] = 0;
        }

        notifyColorChanged();
    }

    /**
     * Sets the brightness (value) component of the HSV color.
     * This should be called when an external control, like a slider, is updated.
     *
     * @param value The brightness value from 0.0 to 1.0.
     */
    public void setBrightness(float value) {
        hsv[2] = Math.max(0f, Math.min(1f, value));
        notifyColorChanged();
    }

    public float getBrightness() {
        return hsv[2];
    }

    public void setOnColorChangedListener(OnColorChangedListener listener) {
        this.onColorChangedListener = listener;
    }

    private void notifyColorChanged() {
        if (onColorChangedListener != null) {
            var color = Color.HSVToColor(hsv);
            var colorString = String.format("#%06X", (0xFFFFFF & color));
            Log.d("RCP", "Notifying color changed: " + color + ", hsv: " + hsv[0] + ", " + hsv[1] + ", " + hsv[2] + ", " + colorString);
            onColorChangedListener.onColorChanged(color);
        }
    }

    public void setColor(String colorString) {
        try {
            var color = Color.parseColor(colorString);
            Color.colorToHSV(color, hsv);
            updateSelectorPositionFromColor();
            notifyColorChanged();
            invalidate();
        } catch (IllegalArgumentException e) {
            Log.e("ColorPickerView", "Invalid color string: " + colorString, e);
        }
    }

    private void updateSelectorPositionFromColor() {
        var hue = hsv[0]; // 0-360
        var saturation = hsv[1]; // 0-1

        var angle = (float) Math.toRadians(hue);
        var distance = saturation * radius;

        selectorX = centerX + (float) (distance * Math.cos(angle));
        selectorY = centerY - (float) (distance * Math.sin(angle));
    }

}
