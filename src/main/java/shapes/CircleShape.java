package shapes;

import javafx.scene.canvas.GraphicsContext;

public class CircleShape extends AbstractShape {

    public CircleShape(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(GraphicsContext gc, boolean selected) {
        applySelectionStyle(gc, selected);
        gc.strokeOval(x, y, width, height);
    }

    @Override
    public boolean contains(double px, double py) {
        double rx = width / 2.0;
        double ry = height / 2.0;
        if (rx <= 0 || ry <= 0) {
            return false;
        }
        double cx = x + rx;
        double cy = y + ry;
        double normalized = Math.pow((px - cx) / rx, 2) + Math.pow((py - cy) / ry, 2);
        return normalized <= 1.0;
    }

    @Override
    public String getType() {
        return "CIRCLE";
    }
}
