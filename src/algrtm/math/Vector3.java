package algrtm.math;

import algrtm.variable.Variable;

public class Vector3 {
    /*
     * ----- x
     * |\
     * | \
     * |  \
     * y   z
     */
    double x, y, z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3(Vector2 v) {
        this(v.x, v.y, 0);
    }
    public Vector3(Vector3 v) {
        this(v.x, v.y, v.z);
    }
    public Vector3() {
        this(0, 0, 0);
    }
    public Vector3(double xyAngle, double xzAngle) {
        this(Math.cos(xyAngle) * Math.cos(xzAngle),
                Math.sin(xyAngle) * Math.cos(xzAngle),
                Math.sin(xzAngle));
    }
    public Vector3(Variable v1, Variable v2, Variable v3) {
        this(v1.getValue(), v2.getValue(), v3.getValue());
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
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void set(Vector3 v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    /** Vector operations **/
    // Add
    public Vector3 add(double dx, double dy, double dz) {
        return new Vector3(x + dx, y + dy, z + dz);
    }
    public Vector3 add(Vector3 v) {
        return new Vector3(x + v.x, y + v.y, z + v.z);
    }
    // Subtract
    public Vector3 sub(double dx, double dy, double dz) {
        return new Vector3(x - dx, y - dy, z - dz);
    }
    public Vector3 sub(Vector3 v) {
        return new Vector3(x - v.x, y - v.y, z - v.z);
    }
    // Multiply and divide
    public Vector3 mlp(double a) {
        return new Vector3(x * a, y * a, z * a);
    }
    public Vector3 div(double a) {
        return new Vector3(x / a, y / a, z / a);
    }
    public Vector3 scalar_mlp(Vector3 v) {
        return new Vector3(x * v.x, y * v.y, z * v.z);
    }
    public Vector3 scalar_div(Vector3 v) {
        return new Vector3(x / v.x, y / v.y, z / v.z);
    }

    /** Vector properties **/
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double toXYAngle() {
        return Math.atan2(y, x);
    }
    public double toXZAngle() {
        return Math.atan2(z, x);
    }

    public Vector3 norm() {
        return div(length());
    }

    public double dot(Vector3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vector3 lerp(Vector3 v, double t) {
        return add(v.sub(this).mlp(t));
    }
    public static Vector3 lerp(Vector3 v1, Vector3 v2, double t) {
        return v1.add(v2.sub(v1).mlp(t));
    }

    /** Object properties **/
    public Vector3 clone() {
        return new Vector3(x, y, z);
    }

    public boolean equals(Vector3 v) {
        return Double.compare(v.x, x) == 0 &&
                Double.compare(v.y, y) == 0 &&
                Double.compare(v.z, z) == 0;
    }
    public boolean equals(Vector3 v, double delta) {
        return Math.abs(v.x - x) <= delta &&
                Math.abs(v.y - y) <= delta &&
                Math.abs(v.z - z) <= delta;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }
}
