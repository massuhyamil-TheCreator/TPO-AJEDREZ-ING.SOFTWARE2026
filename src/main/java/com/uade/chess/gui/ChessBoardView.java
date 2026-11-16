package com.uade.chess.gui;

import com.uade.chess.application.GameStatus;
import com.uade.chess.domain.Position;
import com.uade.chess.domain.pieces.Piece;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.util.function.Consumer;

/** Dibujo y eventos únicamente. a1 es oscura; blancas abajo al iniciar. */
public final class ChessBoardView extends Region {
    private final Button[][] squares = new Button[8][8];
    private final Label[] filesTop = new Label[8];
    private final Label[] filesBottom = new Label[8];
    private final Label[] ranksLeft = new Label[8];
    private final Label[] ranksRight = new Label[8];
    private final Pane frame = new Pane();
    private final PieceRenderer renderer = new PieceRenderer();
    private final Consumer<Position> onClick;
    private boolean flipped;
    private ChessViewState state;

    public ChessBoardView(Consumer<Position> onClick) {
        this.onClick = onClick;
        setMinSize(0, 0);
        setPrefSize(680, 680);
        frame.getStyleClass().add("board-frame");
        frame.setMouseTransparent(true);
        getChildren().add(frame);
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                Button square = new Button();
                square.setMinSize(0, 0);
                square.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                squares[row][column] = square;
                getChildren().add(square);
            }
        }
        for (int index = 0; index < 8; index++) {
            filesTop[index] = coordinate();
            filesBottom[index] = coordinate();
            ranksLeft[index] = coordinate();
            ranksRight[index] = coordinate();
            getChildren().addAll(filesTop[index], filesBottom[index], ranksLeft[index], ranksRight[index]);
        }
    }

    private Label coordinate() {
        Label label = new Label();
        label.getStyleClass().add("coordinate");
        label.setMouseTransparent(true);
        return label;
    }

    public void flip() {
        flipped = !flipped;
        if (state != null) render(state);
    }

    public void render(ChessViewState state) {
        this.state = state;
        MoveEntry last = state.lastMove();
        for (int screenRow = 0; screenRow < 8; screenRow++) {
            for (int screenColumn = 0; screenColumn < 8; screenColumn++) {
                Position position = new Position(flipped ? screenRow : 7 - screenRow,
                        flipped ? 7 - screenColumn : screenColumn);
                Button square = squares[screenRow][screenColumn];
                Piece piece = state.pieceAt(position);
                square.setId("square-" + position);
                square.setAccessibleText(position + ": " + (piece == null ? "vacía"
                        : PresentationText.piece(piece) + " " + PresentationText.color(piece.getColor())));
                square.setOnAction(event -> onClick.accept(position));
                square.getStyleClass().setAll("square",
                        (position.row() + position.column()) % 2 == 0 ? "dark-square" : "light-square");
                if (last != null && (position.equals(last.move().from())
                        || position.equals(last.move().to()))) square.getStyleClass().add("last-move");
                if (piece != null && Character.toLowerCase(piece.getSymbol()) == 'k'
                        && piece.getColor() == state.turn()
                        && (state.status() == GameStatus.CHECK || state.status() == GameStatus.CHECKMATE)) {
                    square.getStyleClass().add("checked-king");
                }
                if (position.equals(state.selected())) square.getStyleClass().add("selected-square");
                StackPane content = new StackPane();
                content.setMouseTransparent(true);
                content.prefWidthProperty().bind(square.widthProperty());
                content.prefHeightProperty().bind(square.heightProperty());
                if (piece != null) content.getChildren().add(renderer.create(piece));
                if (state.legalMoves().contains(position)) {
                    Circle hint = new Circle();
                    hint.radiusProperty().bind(square.widthProperty().multiply(piece == null ? 0.10 : 0.40));
                    hint.getStyleClass().add(piece == null ? "move-dot" : "capture-ring");
                    content.getChildren().add(hint);
                }
                square.setGraphic(content);
            }
        }
        for (int index = 0; index < 8; index++) {
            String file = String.valueOf((char) ('a' + (flipped ? 7 - index : index)));
            String rank = String.valueOf(flipped ? index + 1 : 8 - index);
            filesTop[index].setText(file);
            filesBottom[index].setText(file);
            ranksLeft[index].setText(rank);
            ranksRight[index].setText(rank);
        }
        requestLayout();
    }

    @Override
    protected void layoutChildren() {
        double cell = Math.max(0, Math.floor((Math.min(getWidth(), getHeight()) - 48) / 8));
        double side = cell * 8 + 48;
        double left = Math.floor((getWidth() - side) / 2);
        double top = Math.floor((getHeight() - side) / 2);
        frame.resizeRelocate(left, top, side, side);
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                squares[row][column].resizeRelocate(left + 24 + column * cell,
                        top + 24 + row * cell, cell, cell);
            }
        }
        for (int index = 0; index < 8; index++) {
            filesTop[index].resizeRelocate(left + 24 + index * cell, top, cell, 24);
            filesBottom[index].resizeRelocate(left + 24 + index * cell, top + side - 24, cell, 24);
            ranksLeft[index].resizeRelocate(left, top + 24 + index * cell, 24, cell);
            ranksRight[index].resizeRelocate(left + side - 24, top + 24 + index * cell, 24, cell);
        }
    }
}
