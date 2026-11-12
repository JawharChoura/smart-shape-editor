package command;

import model.DrawingModel;
import shapes.Shape;

public class DeleteCommand implements Command {

    private final DrawingModel model;
    private final Shape shape;
    private int previousIndex = -1;

    public DeleteCommand(DrawingModel model, Shape shape) {
        this.model = model;
        this.shape = shape;
    }

    @Override
    public void execute() {
        previousIndex = model.indexOf(shape);
        if (previousIndex >= 0) {
            model.removeShape(shape);
        }
    }

    @Override
    public void undo() {
        if (previousIndex >= 0) {
            model.addShapeAt(previousIndex, shape);
            model.setSelectedShape(shape);
        }
    }

    @Override
    public String getDescription() {
        return "Suppression de " + shape.getType();
    }
}
