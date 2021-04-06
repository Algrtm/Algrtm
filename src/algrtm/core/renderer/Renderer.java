package algrtm.core.renderer;

import algrtm.Algrtm;
import algrtm.layers.DynamicLayer;
import algrtm.layers.StaticLayer;
import algrtm.math.Calc;
import algrtm.math.Vector3;
import algrtm.math.Vector4;
import algrtm.parallel.Parallel;
import algrtm.util.Graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Renderer {
    Algrtm algrtm;

    int renderWidth, renderHeight;

    BufferedImage imageRender;
    Color backgroundColor;

    ArrayList<DynamicLayer> dynamicLayers;
    ArrayList<BufferedImage> dynamicLayerImages;
    ArrayList<Graphics2D> dynamicLayerGraphics;

    ArrayList<StaticLayer> staticLayers;
    ArrayList<BufferedImage> staticLayerImages;
    ArrayList<Double[]> staticLayerDimensions;

    public Renderer(Algrtm algrtm, int renderWidth, int renderHeight) {
        this.algrtm = algrtm;

        this.renderWidth = renderWidth;
        this.renderHeight = renderHeight;

        imageRender = new BufferedImage(renderWidth, renderHeight, BufferedImage.TYPE_INT_RGB);
        backgroundColor = Color.BLACK;

        dynamicLayers = new ArrayList<>();
        dynamicLayerImages = new ArrayList<>();
        dynamicLayerGraphics = new ArrayList<>();

        staticLayers = new ArrayList<>();
        staticLayerImages = new ArrayList<>();
        staticLayerDimensions = new ArrayList<>();
    }

    // Getters
    public int getRenderWidth() {
        return renderWidth;
    }
    public int getRenderHeight() {
        return renderHeight;
    }

    // Core logic
    public void addDynamicLayer(DynamicLayer dynamicLayer) {
        dynamicLayers.add(dynamicLayer);
        BufferedImage layerImage = new BufferedImage(renderWidth, renderHeight, BufferedImage.TYPE_INT_ARGB);
        dynamicLayerImages.add(layerImage);
        Graphics2D graphics = layerImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        dynamicLayerGraphics.add(graphics);
    }
    public void addStaticLayer(double width, double height, StaticLayer staticLayer) {
        staticLayers.add(staticLayer);
        BufferedImage layerImage = new BufferedImage(renderWidth, renderHeight, BufferedImage.TYPE_INT_ARGB);
        staticLayerImages.add(layerImage);
        staticLayerDimensions.add(new Double[]{width, height});
    }

    public void updateDynamicLayer(int layer) {
        // Render layers
        Graphics2D layerGraphics = this.dynamicLayerGraphics.get(layer);
        dynamicLayers.get(layer).render(layerGraphics, renderWidth, renderHeight, layer);
    }
    public void updateAllStaticLayers() {
        Parallel parallel = new Parallel(x -> {
            Vector4 color;
            for (int y = 0; y < renderHeight; y++) {
                for (int layer = 0; layer < staticLayers.size(); layer++) {
                    double dScale = staticLayerDimensions.get(layer)[0] / renderWidth;
                    color = staticLayers.get(layer).setColor(
                            x * dScale,
                            y * dScale,
                            staticLayerDimensions.get(layer)[0],
                            staticLayerDimensions.get(layer)[1],
                            layer).clone();
                    color.set(
                            Calc.clamp(color.getX(), 0, 1),
                            Calc.clamp(color.getY(), 0, 1),
                            Calc.clamp(color.getZ(), 0, 1),
                            Calc.clamp(color.getT(), 0, 1));
                    color = color.mlp(255);
                    int p = 0;
                    p = p | ((int) color.getT() << 24);
                    p = p | ((int) color.getX() << 16);
                    p = p | ((int) color.getY() << 8);
                    p = p | ((int) color.getZ());
                    staticLayerImages.get(layer).setRGB(x, y, p);
                }
            }
        }, renderWidth);
        parallel.start(4);
    }

    public void updateAll() {
        for (int i = 0; i < dynamicLayers.size(); i++)
            updateDynamicLayer(i);
        updateAllStaticLayers();
    }

    public void updateFrame() {
        // Combining layers
        Graphics2D graphics = imageRender.createGraphics();
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, renderWidth, renderHeight);
        for (BufferedImage image : dynamicLayerImages)
            graphics.drawImage(image, 0, 0, null);
        for (BufferedImage image : staticLayerImages)
            graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
    }

    public void reset() {
        for (Graphics2D g: dynamicLayerGraphics)
            Graphics.clear(g, renderWidth, renderHeight);
        Graphics2D g = imageRender.createGraphics();
        Graphics.clear(g, renderWidth, renderHeight);
        g.dispose();
    }

    public void dispose() {
        for (Graphics2D g: dynamicLayerGraphics)
            g.dispose();
    }

    // Misc
    public BufferedImage getImageRender() {
        BufferedImage copy = new BufferedImage(renderWidth, renderHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = copy.createGraphics();
        g.drawImage(imageRender, 0, 0, null);
        g.dispose();
        return copy;
    }

    public Graphics2D getLayerGraphics(int layer) {
        return dynamicLayerImages.get(layer).createGraphics();
    }

    public void setBackground(Color color) {
        backgroundColor = color;
    }

    public Color getColor(int layer, int x, int y) {
        if (x < 0 || x >= renderWidth || y < 0 || y >= renderHeight)
            return new Color(0, 0, 0, 0);
        return new Color(dynamicLayerImages.get(layer).getRGB(x, y), true);
    }

    public void setRGB(int layer, int x, int y, int rgb) {
        if (x < 0 || x >= renderWidth || y < 0 || y >= renderHeight)
            return;
        dynamicLayerImages.get(layer).setRGB(x, y, rgb);
    }
}
