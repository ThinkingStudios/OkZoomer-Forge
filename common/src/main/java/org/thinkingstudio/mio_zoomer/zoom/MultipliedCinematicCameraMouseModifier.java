package org.thinkingstudio.mio_zoomer.zoom;

import org.thinkingstudio.mio_zoomer.MioZoomerClientMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.SmoothUtil;
import net.minecraft.util.Identifier;
import org.thinkingstudio.zoomerlibrary.api.MouseModifier;

// The implementation of the multiplied cinematic camera
public class MultipliedCinematicCameraMouseModifier implements MouseModifier {
    private static final Identifier MODIFIER_ID = new Identifier(MioZoomerClientMod.MODID + ":multiplied_cinematic_camera");
    private final MinecraftClient client;
    private final SmoothUtil cursorXZoomSmoother = new SmoothUtil();
    private final SmoothUtil cursorYZoomSmoother = new SmoothUtil();
    private boolean active;
    private boolean cinematicCameraEnabled;
    private final double cinematicCameraMultiplier;

    public MultipliedCinematicCameraMouseModifier(double cinematicCameraMultiplier) {
        this.cinematicCameraMultiplier = cinematicCameraMultiplier;
        this.client = MinecraftClient.getInstance();
    }

    @Override
    public Identifier getIdentifier() {
        return MODIFIER_ID;
    }

    @Override
    public boolean getActive() {
        return this.active;
    }

    @Override
    public double applyXModifier(double cursorDeltaX, double cursorSensitivity, double mouseUpdateTimeDelta, double targetDivisor, double transitionMultiplier) {
        if (this.cinematicCameraEnabled) {
            this.cursorXZoomSmoother.clear();
            return cursorDeltaX;
        }
        double smoother = mouseUpdateTimeDelta * cinematicCameraMultiplier * cursorSensitivity;
        return this.cursorXZoomSmoother.smooth(cursorDeltaX, smoother);
    }

    @Override
    public double applyYModifier(double cursorDeltaY, double cursorSensitivity, double mouseUpdateTimeDelta, double targetDivisor, double transitionMultiplier) {
        if (this.cinematicCameraEnabled) {
            this.cursorYZoomSmoother.clear();
            return cursorDeltaY;
        }
        double smoother = mouseUpdateTimeDelta * cinematicCameraMultiplier * cursorSensitivity;
        return this.cursorYZoomSmoother.smooth(cursorDeltaY, smoother);
    }

    @Override
    public void tick(boolean active) {
        this.cinematicCameraEnabled = this.client.options.cinematicCamera;
        if (!active && active != this.active) {
            this.cursorXZoomSmoother.clear();
            this.cursorYZoomSmoother.clear();
        }
        this.active = active;
    }
}
