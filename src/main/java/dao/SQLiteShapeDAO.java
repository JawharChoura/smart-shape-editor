package dao;

import factory.ShapeFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import shapes.Shape;
import utils.DBUtil;

public class SQLiteShapeDAO implements ShapeDAO {

    private static final String DELETE_SHAPES = "DELETE FROM shapes";
    private static final String INSERT_SHAPE = "INSERT INTO shapes(type, x, y, width, height) VALUES(?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SHAPES = "SELECT type, x, y, width, height FROM shapes ORDER BY id";

    private final ShapeFactory shapeFactory;

    public SQLiteShapeDAO(ShapeFactory shapeFactory) {
        this.shapeFactory = shapeFactory;
    }

    @Override
    public void saveShapes(List<Shape> shapes) {
        try (Connection connection = DBUtil.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement deleteStatement = connection.prepareStatement(DELETE_SHAPES)) {
                deleteStatement.executeUpdate();
            }

            try (PreparedStatement insertStatement = connection.prepareStatement(INSERT_SHAPE)) {
                for (Shape shape : shapes) {
                    insertStatement.setString(1, shape.getType());
                    insertStatement.setDouble(2, shape.getX());
                    insertStatement.setDouble(3, shape.getY());
                    insertStatement.setDouble(4, shape.getWidth());
                    insertStatement.setDouble(5, shape.getHeight());
                    insertStatement.addBatch();
                }
                insertStatement.executeBatch();
            }

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde des formes", e);
        }
    }

    @Override
    public List<Shape> loadShapes() {
        List<Shape> result = new ArrayList<>();
        try (Connection connection = DBUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(SELECT_ALL_SHAPES)) {

            while (rs.next()) {
                String type = rs.getString("type");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double width = rs.getDouble("width");
                double height = rs.getDouble("height");
                result.add(shapeFactory.createShape(type, x, y, width, height));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des formes", e);
        }
        return result;
    }
}
