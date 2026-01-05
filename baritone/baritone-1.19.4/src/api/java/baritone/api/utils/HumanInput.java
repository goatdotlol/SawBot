package baritone.api.utils;

import java.util.Random;

public class HumanInput {

    private static final Random random = new Random();

    // curve state
    private long startTime;
    private long duration;
    private Rotation startRot;
    private Rotation targetRot;
    private Rotation controlPoint1;
    private Rotation controlPoint2;
    private boolean isAiming;

    public HumanInput() {
        this.isAiming = false;
    }

    /**
     * Updates the efficient rotation based on human-like bezier curves.
     * 
     * @param current The current player rotation.
     * @param target  The desired target rotation.
     * @return The next rotation step to apply.
     */
    public Rotation update(Rotation current, Rotation target) {
        long now = System.currentTimeMillis();

        // If target changed significantly or we finished previous curve, start new
        // curve
        if (!isAiming || hasTargetChanged(targetRot, target)) {
            startCurve(current, target, now);
        }

        if (now >= startTime + duration) {
            isAiming = false;
            return target;
        }

        float progress = (float) (now - startTime) / duration;

        // Cubic Bezier Curve (P0, P1, P2, P3)
        // P0=start, P3=target
        // P1, P2 are control points adding randomness/curvature

        float u = 1 - progress;
        float tt = progress * progress;
        float uu = u * u;
        float uuu = uu * u;
        float ttt = tt * progress;

        // B(t) = (1-t)^3 P0 + 3(1-t)^2 t P1 + 3(1-t) t^2 P2 + t^3 P3

        float yaw = uuu * startRot.getYaw()
                + 3 * uu * progress * controlPoint1.getYaw()
                + 3 * u * tt * controlPoint2.getYaw()
                + ttt * targetRot.getYaw();

        float pitch = uuu * startRot.getPitch()
                + 3 * uu * progress * controlPoint1.getPitch()
                + 3 * u * tt * controlPoint2.getPitch()
                + ttt * targetRot.getPitch();

        // Apply noise
        float noiseScale = 0.5f * (1 - progress); // Noise reduces as we get closer
        yaw += (random.nextFloat() - 0.5f) * noiseScale;
        pitch += (random.nextFloat() - 0.5f) * noiseScale;

        return new Rotation(yaw, pitch);
    }

    private void startCurve(Rotation start, Rotation target, long now) {
        this.startRot = start;
        this.targetRot = target;
        this.startTime = now;

        // Speed depends on angle difference
        float dist = Math.abs(target.getYaw() - start.getYaw()) + Math.abs(target.getPitch() - start.getPitch());
        // FASTER: Reduced base time and multiplier.
        // Humans are fast, this is "Faster than normal" but still smooth.
        this.duration = (long) (dist * 2.5 + 40 + random.nextInt(30)); // Heuristic optimized for speed

        // Control points deviation
        float deviation = dist * 0.3f;

        this.controlPoint1 = new Rotation(
                start.getYaw() + (target.getYaw() - start.getYaw()) * 0.33f + (random.nextFloat() - 0.5f) * deviation,
                start.getPitch() + (target.getPitch() - start.getPitch()) * 0.33f
                        + (random.nextFloat() - 0.5f) * deviation);

        this.controlPoint2 = new Rotation(
                start.getYaw() + (target.getYaw() - start.getYaw()) * 0.66f + (random.nextFloat() - 0.5f) * deviation,
                start.getPitch() + (target.getPitch() - start.getPitch()) * 0.66f
                        + (random.nextFloat() - 0.5f) * deviation);

        this.isAiming = true;
    }

    private boolean hasTargetChanged(Rotation currentTarget, Rotation newTarget) {
        if (currentTarget == null)
            return true;
        return Math.abs(currentTarget.getYaw() - newTarget.getYaw()) > 1.0
                || Math.abs(currentTarget.getPitch() - newTarget.getPitch()) > 1.0;
    }
}
