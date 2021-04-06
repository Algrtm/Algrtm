package algrtm.math;

import java.awt.*;

public class Particle {
    Vector2 position, velocity;
    Color color = Color.BLACK;

    public Particle(double x, double y) {
        position = new Vector2(x, y);
        velocity = new Vector2();
    }
    public Particle(Vector2 pos) {
        this(pos.x, pos.y);
    }
    public Particle() {
        this(0, 0);
    }

    public Vector2 getPos() {
        return position;
    }
    public Vector2 getVel() {
        return velocity;
    }
    public Color getColor() {
        return color;
    }

    public void setPos(Vector2 position) {
        this.position.set(position);
    }
    public void setVel(Vector2 velocity) {
        this.velocity.set(velocity);
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public void updatePos() {
        position = position.add(velocity);
    }

    public void draw(Graphics2D g, int r) {
        g.setColor(color);
        g.fillOval(
                (int)(position.getX() - r),
                (int)(position.getY() - r),
                2*r, 2*r);
    }
}
