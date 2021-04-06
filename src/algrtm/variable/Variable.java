package algrtm.variable;

import java.util.ArrayList;

public class Variable {
    static int GLOBAL_INDEX = 0;
    int index;
    String name;
    double value, min, max;
    ArrayList<ChangeListener> changeListeners;

    public Variable(String name, double value, double min, double max) {
        this.name = name;
        index = GLOBAL_INDEX++;
        this.value = value;
        this.min = min;
        this.max = max;
        changeListeners = new ArrayList<>();
    }
    public Variable(String name, double value) {
        this(name, value, value-10, value+10);
    }

    // Getters
    public int getIndex() {
        return index;
    }
    public String getName() {
        return name;
    }
    public double getValue() {
        return value;
    }
    public double getMin() {
        return min;
    }
    public double getMax() {
        return max;
    }

    // Setters
    public void setValue(double value) {
        this.value = value;
        for (ChangeListener changeListener : changeListeners)
            changeListener.valueChanged(this.value, value);
    }
    public void setMin(double min) {
        this.min = min;
    }
    public void setMax(double max) {
        this.max = max;
    }

    // Misc
    public void add(double n) {
        setValue(value + n);
    }
    public void sub(double n) {
        setValue(value - n);
    }
    public void mlp(double n) {
        setValue(value * n);
    }
    public void div(double n) {
        setValue(value / n);
    }

    public void addChangeListener(ChangeListener changeListener) {
        changeListeners.add(changeListener);
    }

    @Override
    public String toString() {
        return "Variable{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
