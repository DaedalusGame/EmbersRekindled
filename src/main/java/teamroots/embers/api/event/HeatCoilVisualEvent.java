package teamroots.embers.api.event;

import net.minecraft.tileentity.TileEntity;

import java.awt.*;

public class HeatCoilVisualEvent extends UpgradeEvent {
    Color color;
    int particles;
    float verticalSpeed;

    public HeatCoilVisualEvent(TileEntity tile, Color color, int particles, float verticalSpeed) {
        super(tile);
        this.color = color;
        this.particles = particles;
        this.verticalSpeed = verticalSpeed;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getParticles() {
        return particles;
    }

    public void setParticles(int particles) {
        this.particles = particles;
    }

    public float getVerticalSpeed() {
        return verticalSpeed;
    }

    public void setVerticalSpeed(float verticalSpeed) {
        this.verticalSpeed = verticalSpeed;
    }
}
