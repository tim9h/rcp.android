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

    public static void bounceView(android.view.View view) {
        view.animate()
                .scaleX(1.05f)
                .scaleY(1.05f)
                .setDuration(100)
                .setInterpolator(new android.view.animation.AccelerateInterpolator())
                .withEndAction(() -> view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(300)
                        .setInterpolator(new android.view.animation.OvershootInterpolator())
                        .start())
                .start();
    }

    public static void morphButtonToPill(com.google.android.material.button.MaterialButton button, boolean toPill) {
        var originalRadius = button.getContext().getResources().getDimension(dev.tim9h.rcpandroid.R.dimen.rounded_corners);
        var currentRadius = button.getShapeAppearanceModel().getBottomLeftCornerSize().getCornerSize(new android.graphics.RectF(0, 0, button.getWidth(), button.getHeight()));

        var pillRadius = button.getHeight() / 2f;
        if (pillRadius <= 0)
            pillRadius = button.getContext().getResources().getDisplayMetrics().density * 100;

        var end = toPill ? pillRadius : originalRadius;

        var interpolator = com.google.android.material.motion.MotionUtils.resolveThemeInterpolator(
                button.getContext(),
                com.google.android.material.R.attr.motionEasingEmphasizedInterpolator,
                new android.view.animation.PathInterpolator(0.2f, 0f, 0f, 1f));

        var animator = android.animation.ValueAnimator.ofFloat(currentRadius, end);
        animator.setDuration(500);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(animation -> {
            var value = (float) animation.getAnimatedValue();
            button.setShapeAppearanceModel(
                    button.getShapeAppearanceModel().toBuilder()
                            .setAllCornerSizes(value)
                            .build()
            );
        });
        animator.start();
    }

    public static void setButtonPillShape(com.google.android.material.button.MaterialButton button, boolean isPill) {
        var pillRadius = button.getHeight() / 2f;
        if (pillRadius <= 0)
            pillRadius = button.getContext().getResources().getDisplayMetrics().density * 100;

        var radius = isPill ? pillRadius : button.getContext().getResources().getDimension(dev.tim9h.rcpandroid.R.dimen.rounded_corners);
        button.setShapeAppearanceModel(
                button.getShapeAppearanceModel().toBuilder()
                        .setAllCornerSizes(radius)
                        .build()
        );
    }

}
