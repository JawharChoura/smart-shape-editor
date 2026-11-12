package shapes;

import javafx.scene.canvas.GraphicsContext;

public class RectangleShape extends AbstractShape {

    public RectangleShape(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(GraphicsContext gc, boolean selected) {
        applySelectionStyle(gc, selected);
        gc.strokeRect(x, y, width, height);
    }

    @Override
    public boolean contains(double px, double py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    @Override
    public String getType() {
        return "RECTANGLE";
    }
}
