package algrtm.layers;

import java.awt.Graphics2D;

public interface DynamicLayer {
    void render(Graphics2D g, int width, int height, int layer);
}
