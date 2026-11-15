package com.uade.chess.domain.rules;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import com.uade.chess.domain.pieces.Piece;

import java.util.Map;

/**
 * Única responsabilidad: dado un tablero y un color, decidir si ese rey
 * está en jaque. No valida movimientos ni conoce de turnos — eso es
 * trabajo de MoveValidator.
 */
public final class CheckDetector {

    public boolean isInCheck(Board board, PieceColor color) {
        Position kingPosition = board.findKing(color);

        for (Map.Entry<Position, Piece> entry : board.getOccupiedSquares().entrySet()) {
            Piece piece = entry.getValue();
            if (piece.getColor() == color) {
                continue;
            }
            if (piece.getThreatenedPositions(entry.getKey(), board).contains(kingPosition)) {
                return true;
            }
        }
        return false;
    }
}
