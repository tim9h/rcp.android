package dev.tim9h.rcpandroid.ui.utils;

import android.view.View;

public class AnimationUtils {

    public static final int ANIMATION_DURATION = 500;

    private AnimationUtils() {
    }

    public static void performExpressiveTransition(View view, Runnable onTransition) {
        var interpolator = com.google.android.material.motion.MotionUtils.resolveThemeInterpolator(
                view.getContext(),
                com.google.android.material.R.attr.motionEasingEmphasizedInterpolator,
                new android.view.animation.PathInterpolator(0.2f, 0f, 0f, 1f));

        view.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .alpha(0f)
                .setDuration(ANIMATION_DURATION / 2)
                .setInterpolator(interpolator)
                .withEndAction(() -> {
                    onTransition.run();
                    view.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .alpha(1f)
                            .setDuration(ANIMATION_DURATION)
                            .setInterpolator(interpolator)
                            .start();
                }).start();
    }

}
