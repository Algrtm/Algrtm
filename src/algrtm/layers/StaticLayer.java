package algrtm.layers;

import algrtm.math.Vector4;

public interface StaticLayer {
    /**
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param width canvas width
     * @param height canvas height
     * @param layer layer index
     * @return Color vector4(r, g, b, a) in range [0-1]
     */
    Vector4 setColor(double x, double y, double width, double height, int layer);
}
