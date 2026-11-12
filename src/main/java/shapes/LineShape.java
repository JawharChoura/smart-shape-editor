package shapes;

import javafx.scene.canvas.GraphicsContext;

public class LineShape extends AbstractShape {

    private static final double HIT_TOLERANCE = 6.0;

    public LineShape(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(GraphicsContext gc, boolean selected) {
        applySelectionStyle(gc, selected);
        gc.strokeLine(x, y, x + width, y + height);
    }

    @Override
    public boolean contains(double px, double py) {
        double x1 = x;
        double y1 = y;
        double x2 = x + width;
        double y2 = y + height;

        double lineLengthSquared = Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2);
        if (lineLengthSquared == 0) {
            return Math.hypot(px - x1, py - y1) <= HIT_TOLERANCE;
        }

        double t = ((px - x1) * (x2 - x1) + (py - y1) * (y2 - y1)) / lineLengthSquared;
        t = Math.max(0, Math.min(1, t));

        double projectionX = x1 + t * (x2 - x1);
        double projectionY = y1 + t * (y2 - y1);
        return Math.hypot(px - projectionX, py - projectionY) <= HIT_TOLERANCE;
    }

    @Override
    public String getType() {
        return "LINE";
    }
}
