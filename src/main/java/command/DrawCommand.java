package command;

import model.DrawingModel;
import shapes.Shape;

public class DrawCommand implements Command {

    private final DrawingModel model;
    private final Shape shape;

    public DrawCommand(DrawingModel model, Shape shape) {
        this.model = model;
        this.shape = shape;
    }

    @Override
    public void execute() {
        model.addShape(shape);
        model.setSelectedShape(shape);
    }

    @Override
    public void undo() {
        model.removeShape(shape);
    }

    @Override
    public String getDescription() {
        return "Dessin de " + shape.getType();
    }
}
