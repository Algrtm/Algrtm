package algrtm.math;

public class Calc {
    public static double percent(int value, int max, int precision) {
        double progress = (double) value / max;
        return ((int) (progress * 100 * precision)) / (double) precision;
    }

    public static double clamp(double value, double min, double max) {
        if (value < min) value = min;
        if (value > max) value = max;
        return value;
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) value = min;
        if (value > max) value = max;
        return value;
    }

    public static double map(double value, double from, double to, double scaleFrom, double scaleTo) {
        double delta = (value - from) / (to - from);
        return scaleFrom + (scaleTo - scaleFrom) * delta;
    }
    public static double mapClamp(double value, double from, double to, double scaleFrom, double scaleTo) {
        return map(clamp(value, from, to), from, to, scaleFrom, scaleTo);
    }
}
