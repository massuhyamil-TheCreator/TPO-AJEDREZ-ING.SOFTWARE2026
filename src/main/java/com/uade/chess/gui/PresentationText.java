package com.uade.chess.gui;

import com.uade.chess.domain.MoveFailureReason;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.pieces.Piece;

/** Traducciones y nombres de presentación, separados del backend. */
public final class PresentationText {
    private PresentationText() { }

    public static String color(PieceColor color) {
        return color == PieceColor.WHITE ? "Blancas" : "Negras";
    }

    public static String piece(Piece piece) {
        return switch (Character.toLowerCase(piece.getSymbol())) {
            case 'p' -> "Peón";
            case 'r' -> "Torre";
            case 'n' -> "Caballo";
            case 'b' -> "Alfil";
            case 'q' -> "Dama";
            case 'k' -> "Rey";
            default -> "Pieza";
        };
    }

    public static String failure(MoveFailureReason reason) {
        return switch (reason) {
            case OUT_OF_BOUNDS -> "Esa casilla está fuera del tablero.";
            case EMPTY_ORIGIN -> "No hay una pieza en la casilla de origen.";
            case NOT_YOUR_TURN -> "Elegí una pieza del jugador que tiene el turno.";
            case FRIENDLY_FIRE -> "No podés capturar una pieza propia.";
            case INVALID_PIECE_MOVEMENT -> "Esa pieza no puede moverse a esa casilla.";
            case SELF_CHECK -> "Ese movimiento dejaría a tu rey en jaque.";
        };
    }
}
