package algrtm.math;

import algrtm.variable.Variable;

public class Vector2 {
    double x, y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Vector2(Vector2 v) {
        this(v.x, v.y);
    }
    public Vector2(Vector3 v) {
        this(v.x, v.y);
    }
    public Vector2() {
        this(0,0);
    }
    public Vector2(double angle) {
        this(Math.cos(angle), Math.sin(angle));
    }
    public Vector2(Variable v1, Variable v2) {
        this(v1.getValue(), v2.getValue());
    }

    // Getters
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    // Setters
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public void set(Vector2 v) {
        x = v.x;
        y = v.y;
    }

    /** Vector operations **/
    // Add
    public Vector2 add(double dx, double dy) {
        return new Vector2(x + dx, y + dy);
    }
    public Vector2 add(Vector2 v) {
        return new Vector2(x + v.x, y + v.y);
    }
    public Vector2 add(double angle) {
        return new Vector2(toAngle() + angle).mlp(length());
    }
    // Subtract
    public Vector2 sub(double dx, double dy) {
        return new Vector2(x - dx, y - dy);
    }
    public Vector2 sub(Vector2 v) {
        return new Vector2(x - v.x, y - v.y);
    }
    // Multiply and divide
    public Vector2 mlp(double a) {
        return new Vector2(x * a, y * a);
    }
    public Vector2 div(double a) {
        return new Vector2(x / a, y / a);
    }
    public Vector2 scalar_mlp(Vector2 v) {
        return new Vector2(x * v.x, y * v.y);
    }
    public Vector2 scalar_div(Vector2 v) {
        return new Vector2(x / v.x, y / v.y);
    }

    /** Vector properties **/
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double toAngle() {
        return Math.atan2(y, x);
    }

    public Vector2 norm() {
        return div(length());
    }

    public double dot(Vector2 v) {
        return x * v.x + y * v.y;
    }

    public Vector2 lerp(Vector2 v, double t) {
        return add(v.sub(this).mlp(t));
    }
    public static Vector2 lerp(Vector2 v1, Vector2 v2, double t) {
        return v1.add(v2.sub(v1).mlp(t));
    }

    /** Object properties **/
    public Vector2 clone() {
        return new Vector2(x, y);
    }

    public boolean equals(Vector2 v) {
        return Double.compare(v.x, x) == 0 &&
                Double.compare(v.y, y) == 0;
    }
    public boolean equals(Vector2 v, double delta) {
        return Math.abs(v.x - x) <= delta &&
                Math.abs(v.y - y) <= delta;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
