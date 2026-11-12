package dao;

import java.util.List;
import shapes.Shape;

public interface ShapeDAO {

    void saveShapes(List<Shape> shapes);

    List<Shape> loadShapes();
}
