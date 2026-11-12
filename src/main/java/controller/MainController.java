package controller;

import command.Command;
import command.CommandManager;
import command.DeleteCommand;
import command.DrawCommand;
import dao.ShapeDAO;
import dao.SQLiteShapeDAO;
import factory.ShapeFactory;
import java.nio.file.Path;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.DrawingModel;
import model.ShapeType;
import observer.ActionSubject;
import observer.LoggerObserver;
import shapes.Shape;
import strategy.ConsoleLogger;
import strategy.DBLogger;
import strategy.FileLogger;
import strategy.LoggerStrategy;
import utils.AlertUtil;
import utils.DBUtil;

public class MainController {

    private static final double CANVAS_WIDTH = 820;
    private static final double CANVAS_HEIGHT = 680;
    private static final double MIN_DRAG_DISTANCE = 4.0;

    private final BorderPane root = new BorderPane();
    private final Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
    private final GraphicsContext gc = canvas.getGraphicsContext2D();

    private final DrawingModel model = new DrawingModel();
    private final ShapeFactory shapeFactory = new ShapeFactory();
    private final ShapeDAO shapeDAO = new SQLiteShapeDAO(shapeFactory);
    private final CommandManager commandManager = new CommandManager();

    private final ActionSubject actionSubject = new ActionSubject();
    private final LoggerObserver loggerObserver = new LoggerObserver(new ConsoleLogger());

    private final ComboBox<String> loggerCombo = new ComboBox<>();

    private ShapeType selectedShapeType = ShapeType.RECTANGLE;
    private double startX;
    private double startY;

    public MainController() {
        DBUtil.initializeDatabase();
        actionSubject.addObserver(loggerObserver);
        buildUi();
        setupEvents();
        redraw();
    }

    public BorderPane getView() {
        return root;
    }

    private void buildUi() {
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #f9fafb, #eef2ff);");

        VBox leftPanel = new VBox(12);
        leftPanel.setPadding(new Insets(16));
        leftPanel.setPrefWidth(250);
        leftPanel.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dbe4ee; -fx-border-width: 0 1 0 0;");

        Label title = new Label("Palette de formes");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

        ToggleGroup shapeGroup = new ToggleGroup();
        RadioButton rectangleBtn = createToolButton("Rectangle", shapeGroup, true);
        RadioButton circleBtn = createToolButton("Cercle", shapeGroup, false);
        RadioButton lineBtn = createToolButton("Ligne", shapeGroup, false);

        rectangleBtn.setOnAction(e -> selectedShapeType = ShapeType.RECTANGLE);
        circleBtn.setOnAction(e -> selectedShapeType = ShapeType.CIRCLE);
        lineBtn.setOnAction(e -> selectedShapeType = ShapeType.LINE);

        Button undoBtn = createActionButton("Undo");
        undoBtn.setOnAction(e -> undoAction());

        Button deleteBtn = createActionButton("Delete");
        deleteBtn.setOnAction(e -> deleteSelectedShape());

        Button saveBtn = createActionButton("Save");
        saveBtn.setOnAction(e -> saveShapes());

        Button loadBtn = createActionButton("Load");
        loadBtn.setOnAction(e -> loadShapes());

        Label loggerLabel = new Label("Strategie de journalisation");
        loggerLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #334155;");

        loggerCombo.getItems().addAll("Console", "Fichier", "Base de donnees");
        loggerCombo.getSelectionModel().selectFirst();
        loggerCombo.setMaxWidth(Double.MAX_VALUE);
        loggerCombo.setOnAction(e -> switchLoggerStrategy());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label hint = new Label("Conseil: clique-glisse sur le canvas pour dessiner. Clique simple pour selectionner.");
        hint.setWrapText(true);
        hint.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        leftPanel.getChildren().addAll(
            title,
            rectangleBtn,
            circleBtn,
            lineBtn,
            new Label("Actions"),
            undoBtn,
            deleteBtn,
            saveBtn,
            loadBtn,
            loggerLabel,
            loggerCombo,
            spacer,
            hint
        );

        root.setLeft(leftPanel);
        root.setCenter(canvas);
        BorderPane.setMargin(canvas, new Insets(20));
    }

    private RadioButton createToolButton(String label, ToggleGroup group, boolean selected) {
        RadioButton button = new RadioButton(label);
        button.setToggleGroup(group);
        button.setSelected(selected);
        button.setStyle("-fx-font-size: 14px; -fx-text-fill: #1e293b;");
        return button;
    }

    private Button createActionButton(String label) {
        Button button = new Button(label);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("""
            -fx-background-color: #0ea5e9;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-background-radius: 8;
            -fx-padding: 8 12;
            """);
        return button;
    }

    private void setupEvents() {
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onMousePressed);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::onMouseReleased);
    }

    private void onMousePressed(MouseEvent event) {
        startX = event.getX();
        startY = event.getY();
    }

    private void onMouseReleased(MouseEvent event) {
        double endX = event.getX();
        double endY = event.getY();
        double dragDistance = Math.hypot(endX - startX, endY - startY);

        if (dragDistance < MIN_DRAG_DISTANCE) {
            selectShapeAt(endX, endY);
            redraw();
            return;
        }

        Shape shape = buildShapeFromMouse(startX, startY, endX, endY);
        executeAndTrack(new DrawCommand(model, shape));
        redraw();
    }

    private Shape buildShapeFromMouse(double sx, double sy, double ex, double ey) {
        if (selectedShapeType == ShapeType.LINE) {
            return shapeFactory.createShape("LINE", sx, sy, ex - sx, ey - sy);
        }

        double x = Math.min(sx, ex);
        double y = Math.min(sy, ey);
        double width = Math.abs(ex - sx);
        double height = Math.abs(ey - sy);

        return shapeFactory.createShape(selectedShapeType.name(), x, y, width, height);
    }

    private void selectShapeAt(double x, double y) {
        Shape selected = null;
        for (int i = model.getShapes().size() - 1; i >= 0; i--) {
            Shape candidate = model.getShapes().get(i);
            if (candidate.contains(x, y)) {
                selected = candidate;
                break;
            }
        }
        model.setSelectedShape(selected);
    }

    private void executeAndTrack(Command command) {
        commandManager.executeCommand(command);
        actionSubject.notifyObservers(command.getDescription());
    }

    private void undoAction() {
        Command command = commandManager.undo();
        if (command == null) {
            AlertUtil.showInfo("Undo", "Aucune action a annuler.");
            return;
        }
        actionSubject.notifyObservers("UNDO: " + command.getDescription());
        redraw();
    }

    private void deleteSelectedShape() {
        Shape selected = model.getSelectedShape();
        if (selected == null) {
            AlertUtil.showInfo("Delete", "Selectionne d'abord une forme en cliquant dessus.");
            return;
        }
        executeAndTrack(new DeleteCommand(model, selected));
        redraw();
    }

    private void saveShapes() {
        try {
            shapeDAO.saveShapes(model.getShapesReadOnly());
            actionSubject.notifyObservers("Sauvegarde de " + model.getShapes().size() + " formes");
            AlertUtil.showInfo("Save", "Dessin sauvegarde dans SQLite avec succes.");
        } catch (RuntimeException e) {
            AlertUtil.showError("Erreur Save", e.getMessage());
        }
    }

    private void loadShapes() {
        try {
            model.clear();
            model.getShapes().addAll(shapeDAO.loadShapes());
            commandManager.clearHistory();
            actionSubject.notifyObservers("Chargement de " + model.getShapes().size() + " formes");
            redraw();
            AlertUtil.showInfo("Load", "Dessin charge depuis SQLite.");
        } catch (RuntimeException e) {
            AlertUtil.showError("Erreur Load", e.getMessage());
        }
    }

    private void switchLoggerStrategy() {
        String selected = loggerCombo.getValue();
        LoggerStrategy strategy = switch (selected) {
            case "Fichier" -> new FileLogger(Path.of("logs", "actions.log"));
            case "Base de donnees" -> new DBLogger();
            default -> new ConsoleLogger();
        };
        loggerObserver.setStrategy(strategy);
        actionSubject.notifyObservers("Changement de logger: " + selected);
    }

    private void redraw() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setStroke(Color.web("#e2e8f0"));
        gc.setLineWidth(1);
        for (int x = 0; x < canvas.getWidth(); x += 25) {
            gc.strokeLine(x, 0, x, canvas.getHeight());
        }
        for (int y = 0; y < canvas.getHeight(); y += 25) {
            gc.strokeLine(0, y, canvas.getWidth(), y);
        }

        for (Shape shape : model.getShapes()) {
            boolean isSelected = shape == model.getSelectedShape();
            shape.draw(gc, isSelected);
        }
    }
}
