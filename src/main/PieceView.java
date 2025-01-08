package main;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;

public class PieceView extends StackPane {
    private final GameController gameController;
    private final Color color;
    private final ShapeType shapeType;
    private double startX;
    private double startY;
    private double mouseX;
    private double mouseY;

    public PieceView(GameController controller, Color color, ShapeType shapeType) {
        this.gameController = controller;
        this.color = color;
        this.shapeType = shapeType;
        
        setPrefSize(40, 40);
        setMaxSize(40, 40);
        
        Shape shape = createShape();
        shape.setFill(color);
        
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(4.0);
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        shape.setEffect(dropShadow);
        
        getChildren().add(shape);
        setupDragHandling();
    }
    
    private Shape createShape() {
        double size = 35; 
        
        switch (shapeType) {
            case CIRCLE:
                return new Circle(size/2);
                
            case SQUARE:
                return new Rectangle(size, size);
                
            case TRIANGLE:
                Polygon triangle = new Polygon();
                triangle.getPoints().addAll(
                    size/2, 0.0,
                    0.0, size,
                    size, size
                );
                return triangle;
                
            case HEXAGON:
                Polygon hexagon = new Polygon();
                double radius = size/2;
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
                trapezoid.getPoints().addAll(
                    size * 0.25, 0.0,
                    size * 0.75, 0.0,
                    size, size,
                    0.0, size
                );
                return trapezoid;
                
            default:
                return new Circle(size/2);
        }
    }
    
    private void setupDragHandling() {
        setOnMousePressed(event -> {
            startX = getTranslateX();
            startY = getTranslateY();
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
            setEffect(new DropShadow(8, Color.GOLD));
            toFront();
            event.consume();
        });

        setOnMouseDragged(event -> {
            setTranslateX(startX + event.getSceneX() - mouseX);
            setTranslateY(startY + event.getSceneY() - mouseY);
            event.consume();
        });

        setOnMouseReleased(event -> {
            if (gameController.tryPlacePiece(event.getSceneX(), event.getSceneY(), color, shapeType)) {
                getParent().getChildrenUnmodifiable().remove(this);
            } else {
                setTranslateX(startX);
                setTranslateY(startY);
            }
            setEffect(new DropShadow(4, Color.gray(0.3, 0.5)));
            event.consume();
        });
    }
}