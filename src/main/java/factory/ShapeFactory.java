package factory;

import shapes.CircleShape;
import shapes.LineShape;
import shapes.RectangleShape;
import shapes.Shape;

public class ShapeFactory {

    public Shape createShape(String type, double x, double y, double width, double height) {
        if (type == null) {
            throw new IllegalArgumentException("Le type de forme ne peut pas etre null");
        }

        return switch (type.toUpperCase()) {
            case "RECTANGLE" -> new RectangleShape(x, y, width, height);
            case "CIRCLE" -> new CircleShape(x, y, width, height);
            case "LINE" -> new LineShape(x, y, width, height);
            default -> throw new IllegalArgumentException("Type de forme inconnu: " + type);
        };
    }
}
