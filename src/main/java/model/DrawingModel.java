package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import shapes.Shape;

public class DrawingModel {

    private final List<Shape> shapes = new ArrayList<>();
    private Shape selectedShape;

    public List<Shape> getShapes() {
        return shapes;
    }

    public List<Shape> getShapesReadOnly() {
        return Collections.unmodifiableList(shapes);
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public void addShapeAt(int index, Shape shape) {
        shapes.add(index, shape);
    }

    public void removeShape(Shape shape) {
        shapes.remove(shape);
        if (selectedShape == shape) {
            selectedShape = null;
        }
    }

    public int indexOf(Shape shape) {
        return shapes.indexOf(shape);
    }

    public void clear() {
        shapes.clear();
        selectedShape = null;
    }

    public Shape getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(Shape selectedShape) {
        this.selectedShape = selectedShape;
    }
}
