package algrtm.math;

import algrtm.variable.Variable;

public class Vector4 {
    /*
     * ----- x
     * |\
     * | \
     * |  \
     * y   z
     */
    double x, y, z, t;

    public Vector4(double x, double y, double z, double t) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.t = t;
    }
    public Vector4(Vector2 v) {
        this(v.x, v.y, 0, 0);
    }
    public Vector4(Vector3 v) {
        this(v.x, v.y, v.z, 0);
    }
    public Vector4() {
        this(0, 0, 0, 0);
    }
    public Vector4(double xyAngle, double xzAngle) {
        this(Math.cos(xyAngle) * Math.cos(xzAngle),
                Math.sin(xyAngle) * Math.cos(xzAngle),
                Math.sin(xzAngle), 0);
    }
    public Vector4(Variable v1, Variable v2, Variable v3, Variable v4) {
        this(v1.getValue(), v2.getValue(), v3.getValue(), v4.getValue());
    }

    // Getters
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }
    public double getT() {
        return t;
    }

    // Setters
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setZ(double z) {
        this.z = z;
    }
    public void setT(double t) {
        this.t = t;
    }
    public void set(double x, double y, double z, double t) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.t = t;
    }
    public void set(Vector4 v) {
        x = v.x;
        y = v.y;
        z = v.z;
        t = v.t;
    }

    /** Vector operations **/
    // Add
    public Vector4 add(double dx, double dy, double dz, double dt) {
        return new Vector4(x + dx, y + dy, z + dz, t + dt);
    }
    public Vector4 add(Vector4 v) {
        return new Vector4(x + v.x, y + v.y, z + v.z, t + v.t);
    }
    // Subtract
    public Vector4 sub(double dx, double dy, double dz, double dt) {
        return new Vector4(x - dx, y - dy, z - dz, t - dt);
    }
    public Vector4 sub(Vector4 v) {
        return new Vector4(x - v.x, y - v.y, z - v.z, t - v.t);
    }
    // Multiply and divide
    public Vector4 mlp(double a) {
        return new Vector4(x * a, y * a, z * a, t * a);
    }
    public Vector4 div(double a) {
        return new Vector4(x / a, y / a, z / a, t / a);
    }
    public Vector4 scalar_mlp(Vector4 v) {
        return new Vector4(x * v.x, y * v.y, z * v.z, t * v.t);
    }
    public Vector4 scalar_div(Vector4 v) {
        return new Vector4(x / v.x, y / v.y, z / v.z, t / v.t);
    }

    /** Vector properties **/
    public double length() {
        return Math.sqrt(x * x + y * y + z * z + t * t);
    }

    public double toXYAngle() {
        return Math.atan2(y, x);
    }
    public double toXZAngle() {
        return Math.atan2(z, x);
    }

    public Vector4 norm() {
        return div(length());
    }

    public double dot(Vector4 v) {
        return x * v.x + y * v.y + z * v.z + t * v.t;
    }

    public Vector4 lerp(Vector4 v, double t) {
        return add(v.sub(this).mlp(t));
    }
    public static Vector4 lerp(Vector4 v1, Vector4 v2, double t) {
        return v1.add(v2.sub(v1).mlp(t));
    }

    /** Object properties **/
    public Vector4 clone() {
        return new Vector4(x, y, z, t);
    }

    public boolean equals(Vector4 v) {
        return Double.compare(v.x, x) == 0 &&
                Double.compare(v.y, y) == 0 &&
                Double.compare(v.z, z) == 0 &&
                Double.compare(v.t, t) == 0;
    }
    public boolean equals(Vector4 v, double delta) {
        return Math.abs(v.x - x) <= delta &&
                Math.abs(v.y - y) <= delta &&
                Math.abs(v.z - z) <= delta &&
                Math.abs(v.t - t) <= delta;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + "," + t + ")";
    }
}

