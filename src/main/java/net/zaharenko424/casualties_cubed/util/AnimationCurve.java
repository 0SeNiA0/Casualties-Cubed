package net.zaharenko424.casualties_cubed.util;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class AnimationCurve {

    public final WrapMode preWrapMode;
    public final WrapMode postWrapMode;
    public final List<Keyframe> keyframes;
    public final Keyframe first;
    public final Keyframe last;

    public AnimationCurve(List<Keyframe> keyframes) {
        this(keyframes, WrapMode.CLAMP_FOREVER, WrapMode.CLAMP_FOREVER);
    }

    public AnimationCurve(List<Keyframe> keyframes, WrapMode preWrapMode, WrapMode postWrapMode) {
        this.preWrapMode = preWrapMode;
        this.postWrapMode = postWrapMode;
        this.keyframes = List.copyOf(keyframes);
        assert !keyframes.isEmpty();

        first = keyframes.get(0);
        last = keyframes.get(keyframes.size() - 1);
    }

    public float evaluate(float time) {
        if (keyframes.size() == 1) return first.value();

        if (time < first.time()) {
            if (preWrapMode.isClamp()) return first.value();

            if (preWrapMode == WrapMode.LOOP) {
                float duration = last.time() - first.time();
                time = (time - first.time()) % duration + duration;
            }
        }

        if (time > last.time()) {
            if (postWrapMode.isClamp()) return last.value();

            if (postWrapMode == WrapMode.LOOP) {
                time = (time - first.time()) % (last.time() - first.time()) + first.time();
            }
        }

        for (int i = 1; i < keyframes.size(); i++) {
            if (keyframes.get(i).time() < time) continue;

            //ith keyframe is keyframe2 with keyframe.time >= time so prev must have been < time
            return evaluate(keyframes.get(i - 1), keyframes.get(i), time);
        }

        return 0;
    }

    public static float evaluate(Keyframe keyframe1, Keyframe keyframe2, float time) {
        float dt = keyframe2.time() - keyframe1.time();
        if (dt <= 0f) return keyframe1.value();

        // 1. Determine weights based on the WeightedMode.
        // If unweighted, Unity effectively uses exactly 1/3 of the time segment.
        float w0 = keyframe1.weightedMode().isOut() ? keyframe1.outWeight() : (1f / 3f);
        float w1 = keyframe2.weightedMode().isIn() ? keyframe2.inWeight() : (1f / 3f);

        // Clamp weights to prevent the curve from looping back on itself
        w0 = Math.max(0f, Math.min(w0, 1f));
        w1 = Math.max(0f, Math.min(w1, 1f));

        // Prevent control points from crossing over each other
        if (w0 + w1 > 1f) {
            float overage = (w0 + w1) - 1f;
            w0 -= overage * 0.5f;
            w1 -= overage * 0.5f;
        }

        // 2. Setup 2D Bezier control points
        // X points (normalized to 0-1 range for the solver)
        float p1x = w0;
        float p2x = 1f - w1;

        // Y points (actual values)
        // Tangent is dy/dx. If we advance X by (w0 * dt), we advance Y by (Tangent * w0 * dt).
        float p0y = keyframe1.value();
        float p1y = keyframe1.value() + (keyframe1.outTangent() * w0 * dt);
        float p2y = keyframe2.value() - (keyframe2.inTangent() * w1 * dt);
        float p3y = keyframe2.value();

        // 3. Find the parametric 't' that corresponds to the target X time
        float targetX = (time - keyframe1.time()) / dt;
        float t = findParametricTForX(targetX, p1x, p2x);

        // 4. Evaluate the Y(t) value using the standard Cubic Bezier formula
        float mt = 1f - t;
        return (mt * mt * mt * p0y) +
                (3f * mt * mt * t * p1y) +
                (3f * mt * t * t * p2y) +
                (t * t * t * p3y);
    }

    private static float findParametricTForX(float targetX, float p1x, float p2x) {
        // Fast path: if weights are standard 1/3, it's a linear X mapping (t == X)
        if (Math.abs(p1x - 1f / 3f) < 1e-4f && Math.abs(p2x - 2f / 3f) < 1e-4f) return targetX;

        float t = targetX; // Initial guess

        // Newton-Raphson solver (fast, but can diverge)
        for (int i = 0; i < 5; i++) {
            float mt = 1f - t;
            float t2 = t * t;
            float mt2 = mt * mt;

            // Current X(t)
            float currentX = (3f * mt2 * t * p1x) + (3f * mt * t2 * p2x) + (t2 * t);
            float error = currentX - targetX;

            if (Math.abs(error) < 1e-5f) return t;

            // Derivative of X with respect to t: X'(t)
            float dx = (3f * mt2 * p1x) + (6f * mt * t * (p2x - p1x)) + (3f * t2 * (1f - p2x));

            if (dx < 1e-5f) break; // Derivative too small, avoid division by zero and fallback

            t -= error / dx;
        }

        // Bisection solver (slower, but guaranteed to converge)
        // Used as a fallback if Newton-Raphson failed to reach the tolerance
        float low = 0f;
        float high = 1f;
        t = targetX;

        for (int i = 0; i < 15; i++) {
            float mt = 1f - t;
            float currentX = (3f * mt * mt * t * p1x) + (3f * mt * t * t * p2x) + (t * t * t);

            if (Math.abs(currentX - targetX) < 1e-5f) break;

            if (currentX > targetX) high = t;
            else low = t;

            t = (high + low) * 0.5f;
        }

        return t;
    }
}
