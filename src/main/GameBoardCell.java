package main;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;



public class GameBoardCell extends StackPane {
    private Color color;
    private ShapeType shapeType;
    private boolean empty;
    private Shape shapeNode;
    private String difficulty;
    private Shape piece;

    public GameBoardCell(Color color, ShapeType shapeType, String difficulty) {
        this.color = color;
        this.shapeType = shapeType;
        this.difficulty = difficulty;
        this.empty = difficulty.equals("LEVEL 1");
        this.piece = createShapeOutline();

        // Set up base cell properties
        setPrefSize(90, 80);
        setMaxSize(80, 80);
        setPadding(new Insets(5));

        // Set cell background and border
        setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        setBorder(new Border(new BorderStroke(
            Color.LIGHTGRAY,
            BorderStrokeStyle.SOLID,
            CornerRadii.EMPTY,
            new BorderWidths(1)
        )));

        shapeNode = difficulty.equals("LEVEL 1") ? createEmptyCell() : createFilledCell();
        getChildren().add(shapeNode);

        // Set up mouse interactions
        setupMouseHandlers();
    }

    private void setupMouseHandlers() {
        setOnMouseEntered(e -> {
            if (empty) {
                setBackground(new Background(new BackgroundFill(
                    Color.rgb(245, 245, 245),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
                )));
            }
        });

        setOnMouseExited(e -> {
            if (empty) {
                setBackground(new Background(new BackgroundFill(
                    Color.WHITE,
                    CornerRadii.EMPTY,
                    Insets.EMPTY
                )));
            }
        });

        setOnMouseClicked(e -> {
            if (difficulty.equals("LEVEL 2") && !empty) {
                removePiece();
            }
        });
    }

    private Shape createEmptyCell() {
        Shape shape = createShapeOutline();
        shape.setFill(null);
        shape.setStroke(color);
        shape.setStrokeWidth(2);
        return shape;
    }

    private Shape createFilledCell() {
        Shape shape = createShapeOutline();
        shape.setFill(color);
        shape.setStroke(Color.TRANSPARENT);
        return shape;
    }

    private Shape createShapeOutline() {
        double width = getPrefWidth() - 10;
        double height = getPrefHeight() - 10;
        double minSize = Math.min(width, height);
        double shapeSize = minSize * 0.7;

        switch (this.shapeType) {
            case CIRCLE:
                return new Circle(shapeSize / 2);
            case SQUARE:
                return new Rectangle(shapeSize, shapeSize);
            case TRIANGLE:
                Polygon triangle = new Polygon();
                triangle.getPoints().addAll(
                    shapeSize / 2, 0.0,
                    0.0, shapeSize,
                    shapeSize, shapeSize
                );
                return triangle;
            case HEXAGON:
                Polygon hexagon = new Polygon();
                double radius = shapeSize / 2;
                for (int i = 0; i < 6; i++) {
                    double angle = 2.0 * Math.PI * i / 6;
                    hexagon.getPoints().addAll(
                        radius + radius * Math.cos(angle),
                        radius + radius * Math.sin(angle)
                    );
                }
                return hexagon;
            case TRAPEZOID:
                Polygon trapezoid = new Polygon();
                double topWidth = shapeSize * 0.7;
                double bottomWidth = shapeSize;
                trapezoid.getPoints().addAll(
                    -topWidth / 2, -shapeSize / 2,    // Top left
                    topWidth / 2, -shapeSize / 2,     // Top right
                    bottomWidth / 2, shapeSize / 2,   // Bottom right
                    -bottomWidth / 2, shapeSize / 2   // Bottom left
                );
                return trapezoid;
            default:
                return new Circle(shapeSize / 2);
        }
    }

    public void placePiece(GamePiece piece) {
        if (!empty) {
            return;
        }

        getChildren().clear();

        // Create and style the filled shape
        Shape filledShape = createShapeOutline();
        filledShape.setFill(piece.getColor());
        filledShape.setStroke(Color.TRANSPARENT);

        // Add drop shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        filledShape.setEffect(dropShadow);

        getChildren().add(filledShape);
        this.empty = false;

        // Update background to show filled state
        setBackground(new Background(new BackgroundFill(
            Color.WHITE,
            CornerRadii.EMPTY,
            Insets.EMPTY
        )));
    }

    public void removePiece() {
        this.empty = true;
        this.piece = null;
        setBackground(new Background(new BackgroundFill(
            Color.WHITE,
            CornerRadii.EMPTY,
            Insets.EMPTY
        )));
        setBorder(new Border(new BorderStroke(
            Color.LIGHTGRAY,
            BorderStrokeStyle.SOLID,
            CornerRadii.EMPTY,
            new BorderWidths(1)
        )));
        // Clear the inner shape color
        if (shapeNode != null) {
            shapeNode.setFill(null);
            shapeNode.setStroke(color);
        }
        // Mark the cell as permanently empty
        this.setDisable(true);
    }

    public void highlightAsValidTarget() {
        if (empty) {
            setBackground(new Background(new BackgroundFill(
                Color.rgb(232, 245, 233), // Light green
                CornerRadii.EMPTY,
                Insets.EMPTY
            )));
            setBorder(new Border(new BorderStroke(
                Color.rgb(76, 175, 80), // Darker green
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(2)
            )));
        }
    }

    public void removeHighlight() {
        if (empty) {
            setBackground(new Background(new BackgroundFill(
                Color.WHITE,
                CornerRadii.EMPTY,
                Insets.EMPTY
            )));
            setBorder(new Border(new BorderStroke(
                Color.LIGHTGRAY,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(1)
            )));
        }
    }

    public void clear() {
        getChildren().clear();
        shapeNode = createEmptyCell();
        getChildren().add(shapeNode);
        empty = true;

        // Reset background and border
        setBackground(new Background(new BackgroundFill(
            Color.WHITE,
            CornerRadii.EMPTY,
            Insets.EMPTY
        )));
        setBorder(new Border(new BorderStroke(
            Color.LIGHTGRAY,
            BorderStrokeStyle.SOLID,
            CornerRadii.EMPTY,
            new BorderWidths(1)
        )));
    }

    public boolean matches(GamePiece piece) {
        return this.color.equals(piece.getColor()) && this.shapeType == piece.getShape();
    }

    public boolean isEmpty() {
        return empty;
    }

    public Color getColor() {
        return color;
    }

    public ShapeType getShapeType() {
        return shapeType;
    }

    // Method to check if the color of the cell and the rolled color match
    public boolean matchesColor(Color rolledColor) {
        return this.color.equals(rolledColor);
    }



}