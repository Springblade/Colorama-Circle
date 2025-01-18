package main;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;

public class PlayerBoard extends FlowPane {
    private final GameController gameController;
    private boolean isCurrentTurn;
    private double pieceSpacing = 5;
    private Orientation orientation = Orientation.HORIZONTAL;

    public PlayerBoard(GameController gameController) {
        this.gameController = gameController;
        this.isCurrentTurn = false;
        
        this.setAlignment(Pos.CENTER);
        this.setRowValignment(VPos.CENTER);
        this.setColumnHalignment(HPos.CENTER);
        this.setHgap(2);
        this.setVgap(2);
        this.setPadding(new Insets(5));
        this.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-width: 2;");
    }

    public void customizeOrientation(Orientation orientation) {
        this.orientation = orientation;
        this.setOrientation(orientation);
        
        if (orientation == Orientation.VERTICAL) {
            // For vertical orientation, set preferred wrap length
            this.setPrefWrapLength(40); // Adjust this value based on your piece size
            this.setVgap(5);
            this.setHgap(2);
        } else {
            // For horizontal orientation
            this.setPrefWrapLength(USE_PREF_SIZE);
            this.setHgap(5);
            this.setVgap(2);
        }       
        this.setRowValignment(VPos.CENTER);
        this.setColumnHalignment(HPos.CENTER);
    }

    public void addPiece(Color color, ShapeType shapeType) {
        Shape shape = createShape(shapeType);
        shape.setFill(color);
        
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(4.0);
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        shape.setEffect(dropShadow);

        makeDraggable(shape, color, shapeType);
        
        double xPos = pieceSpacing;
        for (Node node : getChildren()) {
            xPos += node.getBoundsInLocal().getWidth() + pieceSpacing;
        }
        
        shape.setLayoutX(xPos);
        shape.setLayoutY(getHeight() / 2 - shape.getBoundsInLocal().getHeight() / 2);
        
        this.getChildren().add(shape);
    }

    private Shape createShape(ShapeType shapeType) {
        double baseSize = 40;
        
        switch (shapeType) {
            case CIRCLE:
                Circle circle = new Circle(baseSize / 2);
                circle.setCenterX(baseSize / 2);
                circle.setCenterY(baseSize / 2);
                return circle;
                
            case SQUARE:
                Rectangle square = new Rectangle(baseSize, baseSize);
                return square;
                
            case TRIANGLE:
                Polygon triangle = new Polygon();
                triangle.getPoints().addAll(
                    baseSize / 2, 0.0,      
                    0.0, baseSize,          
                    baseSize, baseSize      
                );
                return triangle;
                
            case HEXAGON:
                Polygon hexagon = new Polygon();
                double radius = baseSize / 2;
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
                    baseSize * 0.25, 0.0,    
                    baseSize * 0.75, 0.0,    
                    baseSize, baseSize,      
                    0.0, baseSize            
                );
                return trapezoid;
                
            default:
                return new Circle(baseSize / 2);
        }
    }

    private void makeDraggable(Shape piece, Color color, ShapeType shapeType) {
        final Delta dragDelta = new Delta();
    
        // Set up hover effect
        piece.setOnMouseEntered(e -> {
            if (isCurrentTurn) {
                piece.setCursor(javafx.scene.Cursor.HAND);
                piece.setEffect(new DropShadow(6, Color.GOLD));
            }
        });
    
        piece.setOnMouseExited(e -> {
            if (isCurrentTurn && !piece.isPressed()) {
                piece.setEffect(new DropShadow(4, Color.gray(0.3, 0.5)));
            }
        });
    
        piece.setOnMousePressed(event -> {
            if (!isCurrentTurn) {
                event.consume();
                return;
            }
    
            // Record initial mouse position relative to piece's parent
            dragDelta.mouseX = event.getSceneX();
            dragDelta.mouseY = event.getSceneY();
    
            // Record the initial position of the piece
            dragDelta.initialTranslateX = piece.getTranslateX();
            dragDelta.initialTranslateY = piece.getTranslateY();
    
            // Visual feedback for dragging
            piece.setOpacity(0.7);
            piece.toFront();
            piece.setEffect(new DropShadow(8, Color.GOLD));
            event.consume();
        });
    
        piece.setOnMouseDragged(event -> {
            if (!isCurrentTurn) {
                event.consume();
                return;
            }
    
            // Calculate the distance moved from the initial press
            double deltaX = event.getSceneX() - dragDelta.mouseX;
            double deltaY = event.getSceneY() - dragDelta.mouseY;
    
            // Update position relative to the initial position
            piece.setTranslateX(dragDelta.initialTranslateX + deltaX);
            piece.setTranslateY(dragDelta.initialTranslateY + deltaY);
    
            event.consume();
        });
    
        piece.setOnMouseReleased(event -> {
            if (!isCurrentTurn) {
                event.consume();
                return;
            }
    
            piece.setOpacity(1.0);
    
            // Try to place the piece based on difficulty level
            if ("LEVEL 1".equals(gameController.getDifficulty())) {
                if (gameController.tryPlacePiece(event.getSceneX(), event.getSceneY(), color, shapeType)) {
                    this.getChildren().remove(piece);
                } else {
                    // Reset position if placement fails
                    piece.setTranslateX(0);
                    piece.setTranslateY(0);
                }
            } else if ("LEVEL 2".equals(gameController.getDifficulty())) {
                // Try to pick the piece for Level 2
                if (gameController.tryPickPiece(event.getSceneX(), event.getSceneY(), gameController.getRolledColor())) {
                    this.getChildren().remove(piece);
                } else {
                    // Reset position if picking fails
                    piece.setTranslateX(0);
                    piece.setTranslateY(0);
                }
            }
                
            piece.setEffect(new DropShadow(4, Color.gray(0.3, 0.5)));
            event.consume();
        });
    }
    
    public void setCurrentTurn(boolean isCurrentTurn) {
        this.isCurrentTurn = isCurrentTurn;
        if (isCurrentTurn) {
            this.setStyle("-fx-background-color: white; -fx-border-color: gold; -fx-border-width: 3;");
            
            // Trigger dice roll if the game is in Level 2
            if ("LEVEL 2".equals(gameController.getDifficulty())) {
                gameController.handleDiceRoll();
            }
        } else {
            this.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-width: 2;");
        }
        for (Node node : this.getChildren()) {
            if (node instanceof Shape) {
                node.setCursor(isCurrentTurn ? javafx.scene.Cursor.HAND : javafx.scene.Cursor.DEFAULT);
            }
        }
    }

    public boolean isCurrentTurn() {
        return isCurrentTurn;
    }

    private static class Delta {
        double mouseX = 0;
        double mouseY = 0;
        double initialTranslateX;
        double initialTranslateY;
    }
}
