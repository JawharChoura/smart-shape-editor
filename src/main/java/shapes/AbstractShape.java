package shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class AbstractShape implements Shape {

    protected final double x;
    protected final double y;
    protected final double width;
    protected final double height;

    protected AbstractShape(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void applySelectionStyle(GraphicsContext gc, boolean selected) {
        gc.setLineWidth(selected ? 3.0 : 2.0);
        gc.setStroke(selected ? Color.CRIMSON : Color.web("#0f172a"));
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }
}
