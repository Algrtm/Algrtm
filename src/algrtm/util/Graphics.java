package algrtm.util;

import algrtm.Algrtm;
import algrtm.math.Vector4;
import algrtm.parallel.Parallel;

import java.awt.*;

public class Graphics {
    /**
     * Scene fading with specified rate.
     * @param algrtm Catharsis object.
     * @param layer index of a layer, to apply fade to.
     * @param rate fading rate in range [1-254]. (Slow to high).
     * @param threads multithreading.
     */
    public static void fadeScene(Algrtm algrtm, int layer, double rate, int threads) {
        if (rate < 1 || rate > 254)
            throw new IllegalArgumentException("Rate " + rate + " out of bounds. [1-254]");
        double finalRate = 1d - rate / 255d;
        Parallel parallelFade = new Parallel(x -> {
            for (int y = 0; y < algrtm.getHeight(); y++) {
                Color color = algrtm.getColor(layer, x, y);
                algrtm.setColor(0, x, y, new Color(
                        (int) (color.getRed() * finalRate),
                        (int) (color.getGreen() * finalRate),
                        (int) (color.getBlue() * finalRate),
                        color.getAlpha()
                ));
            }
        }, algrtm.getWidth());

        parallelFade.start(threads);
    }

    public static void clear(Graphics2D g, int width, int height) {
        g.setBackground(new Color(255, 255, 255, 0));
        g.clearRect(0, 0, width, height);
    }

    public static void setBackground(Graphics2D g, int width, int height, Color backgroundColor) {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, width, height);
    }

    public static Vector4 colorToVector(Color color) {
        return new Vector4(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).div(255d);
    }
}
