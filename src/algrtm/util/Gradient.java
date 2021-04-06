package algrtm.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class Gradient {
    ArrayList<Color> gradient;
    double value, changeRate;
    int c1, c2;

    public Gradient(double changeRate) {
        gradient = new ArrayList<>();

        value = 0d;
        this.changeRate = changeRate;
        c1 = 0;
        c2 = 0;
    }

    public void setStart(int index) {
        value = (double) index / gradient.size();
    }

    public void addColor(Color color) {
        gradient.add(color);
    }
    public void addAll(Color[] colors) {
        gradient.addAll(Arrays.asList(colors));
    }

    public void forward() {
        value += changeRate;

        if (gradient.size() > 1) {
            c1 = (int) Math.floor(value * gradient.size());
            c2 = (int) Math.ceil(value * gradient.size());
            if (c2 == gradient.size())
                c2 = 0;
            if (c1 == gradient.size()) {
                value = changeRate;
                c1 = (int) Math.floor(value * gradient.size());
                c2 = (int) Math.ceil(value * gradient.size());
            }
        } else {
            c1 = 0;
            c2 = 0;
        }
    }

    private double getProgress() {
        double progress = value * gradient.size();
        progress = progress % gradient.size();
        progress -= c1;
        return progress;
    }

    public Color getNext() {
        forward();
        double progress = getProgress();
        return new Color(
                (int) ((1d-progress) * gradient.get(c1).getRed() + (progress) * gradient.get(c2).getRed()),
                (int) ((1d-progress) * gradient.get(c1).getGreen() + (progress) * gradient.get(c2).getGreen()),
                (int) ((1d-progress) * gradient.get(c1).getBlue() + (progress) * gradient.get(c2).getBlue())
        );
    }

    public Color getCurrent() {
        double progress = getProgress();
        return new Color(
                (int) ((1d-progress) * gradient.get(c1).getRed() + (progress) * gradient.get(c2).getRed()),
                (int) ((1d-progress) * gradient.get(c1).getGreen() + (progress) * gradient.get(c2).getGreen()),
                (int) ((1d-progress) * gradient.get(c1).getBlue() + (progress) * gradient.get(c2).getBlue())
        );
    }

    public Gradient copy() {
        Gradient gradient = new Gradient(changeRate);
        gradient.gradient = this.gradient;
        return gradient;
    }

    public static Color blend(Color color1, Color color2, double percent) {
        return new Color(
                (int) ((1d-percent) * color1.getRed()   + (percent) * color2.getRed()),
                (int) ((1d-percent) * color1.getGreen() + (percent) * color2.getGreen()),
                (int) ((1d-percent) * color1.getBlue()  + (percent) * color2.getBlue()),
                (int) ((1d-percent) * color1.getAlpha() + (percent) * color2.getAlpha()));
    }
    public static Color blend(Color color1, Color color2, double percent, int customAlpha) {
        return new Color(
                (int) ((1d-percent) * color1.getRed() + (percent) * color2.getRed()),
                (int) ((1d-percent) * color1.getGreen() + (percent) * color2.getGreen()),
                (int) ((1d-percent) * color1.getBlue() + (percent) * color2.getBlue()),
                customAlpha);
    }
}
