package com.uade.chess.gui;

import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.pieces.Piece;
import javafx.scene.Group;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineJoin;

import java.util.Map;

/** Siluetas vectoriales propias: no requieren fuentes de símbolos ni imágenes externas. */
public final class PieceRenderer {
    private static final Map<Character, String> SILHOUETTES = Map.of(
            'p', "M24 18 A8 8 0 1 1 40 18 A8 8 0 1 1 24 18 Z "
                    + "M25 28 L39 28 L37 34 Q37 41 44 48 L20 48 Q27 41 27 34 Z "
                    + "M19 49 L45 49 L48 55 L16 55 Z",
            'r', "M17 10 L24 10 L24 16 L29 16 L29 10 L35 10 L35 16 L40 16 L40 10 "
                    + "L47 10 L47 23 L42 27 L40 47 L24 47 L22 27 L17 23 Z "
                    + "M20 48 L44 48 L48 55 L16 55 Z",
            'n', "M20 47 Q21 36 32 31 L26 28 L18 31 L13 25 L22 14 L28 12 L30 5 "
                    + "L35 12 Q45 17 46 31 L45 47 Z M19 49 L46 49 L49 55 L16 55 Z",
            'b', "M32 5 Q48 18 41 27 L35 31 L37 37 Q38 42 44 47 L20 47 "
                    + "Q26 42 27 37 L29 31 L23 27 Q16 18 32 5 Z "
                    + "M19 49 L45 49 L48 55 L16 55 Z",
            'q', "M15 14 L25 24 L32 11 L39 24 L49 14 L43 36 L21 36 Z "
                    + "M23 39 L41 39 Q39 43 44 48 L20 48 Q25 43 23 39 Z "
                    + "M18 50 L46 50 L49 56 L15 56 Z "
                    + "M12 11 A3 3 0 1 1 18 11 A3 3 0 1 1 12 11 Z "
                    + "M29 8 A3 3 0 1 1 35 8 A3 3 0 1 1 29 8 Z "
                    + "M46 11 A3 3 0 1 1 52 11 A3 3 0 1 1 46 11 Z",
            'k', "M29 4 L35 4 L35 10 L41 10 L41 16 L35 16 L35 21 L29 21 L29 16 "
                    + "L23 16 L23 10 L29 10 Z M21 23 Q17 20 17 27 Q18 33 24 36 "
                    + "L40 36 Q46 33 47 27 Q47 20 43 23 Q32 28 21 23 Z "
                    + "M25 39 L39 39 Q38 44 44 48 L20 48 Q26 44 25 39 Z "
                    + "M18 50 L46 50 L49 56 L15 56 Z"
    );
    private static final Map<Character, String> DETAILS = Map.of(
            'r', "M23 27 L41 27 M24 43 L40 43",
            'n', "M28 19 L31 19 M35 23 Q39 31 34 40",
            'b', "M35 13 L29 23 M26 35 L38 35",
            'q', "M23 32 L41 32",
            'k', "M25 33 L39 33"
    );

    public Region create(Piece piece) {
        return new Sprite(piece);
    }

    private static final class Sprite extends Region {
        private final Group drawing = new Group();

        private Sprite(Piece piece) {
            setMinSize(0, 0);
            setPrefSize(64, 64);
            setMouseTransparent(true);
            Rectangle bounds = new Rectangle(64, 64, Color.TRANSPARENT);
            drawing.getChildren().add(bounds);
            char symbol = Character.toLowerCase(piece.getSymbol());
            boolean white = piece.getColor() == PieceColor.WHITE;
            Color outline = Color.web(white ? "#253b35" : "#eadbc3");
            SVGPath silhouette = new SVGPath();
            silhouette.setContent(SILHOUETTES.getOrDefault(symbol, SILHOUETTES.get('p')));
            silhouette.setFill(Color.web(white ? "#fff4dc" : "#203c35"));
            silhouette.setStroke(outline);
            silhouette.setStrokeWidth(1.8);
            silhouette.setStrokeLineJoin(StrokeLineJoin.ROUND);
            drawing.getChildren().add(silhouette);
            if (DETAILS.containsKey(symbol)) {
                SVGPath detail = new SVGPath();
                detail.setContent(DETAILS.get(symbol));
                detail.setFill(Color.TRANSPARENT);
                detail.setStroke(outline);
                detail.setStrokeWidth(1.6);
                drawing.getChildren().add(detail);
            }
            getChildren().add(drawing);
        }

        @Override
        protected void layoutChildren() {
            double scale = Math.min(getWidth(), getHeight()) / 64 * 0.88;
            drawing.setScaleX(scale);
            drawing.setScaleY(scale);
            drawing.setLayoutX((getWidth() - 64) / 2);
            drawing.setLayoutY((getHeight() - 64) / 2);
        }
    }
}
