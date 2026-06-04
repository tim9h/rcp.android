package dev.tim9h.rcpandroid.ui.utils;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

public class AnimationUtils {

    public static final int ANIMATION_DURATION = 500;

    private AnimationUtils() {
    }

    public static void performExpressiveTransition(View view, Runnable onTransition) {
        view.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .alpha(0f)
                .setDuration(ANIMATION_DURATION / 2)
                .setInterpolator(new AccelerateInterpolator())
                .withEndAction(() -> {
                    onTransition.run();
                    view.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .alpha(1f)
                            .setDuration(ANIMATION_DURATION)
                            .setInterpolator(new OvershootInterpolator(1.2f))
                            .start();
                }).start();
    }

}
