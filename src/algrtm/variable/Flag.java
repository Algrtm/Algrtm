package algrtm.variable;

public class Flag {
    boolean value;

    public Flag(boolean value) {
        this.value = value;
    }
    public Flag() {
        this(false);
    }

    // Getters
    public boolean isSet() {
        return value;
    }

    // Setters
    public void set(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Flag{" +
                "value=" + value +
                '}';
    }
}
