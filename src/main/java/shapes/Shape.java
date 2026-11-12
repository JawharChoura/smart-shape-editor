package shapes;

import javafx.scene.canvas.GraphicsContext;

public interface Shape {

    void draw(GraphicsContext gc, boolean selected);

    boolean contains(double px, double py);

    String getType();

    double getX();

    double getY();

    double getWidth();

    double getHeight();
}
