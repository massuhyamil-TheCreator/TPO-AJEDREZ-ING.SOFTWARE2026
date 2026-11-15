package com.uade.chess.domain.movement;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.Direction;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Lógica de movimiento deslizante, compartida por Torre, Alfil y Reina
 * mediante COMPOSICIÓN de direcciones (no por herencia ni duplicación):
 * cada pieza le pasa su propio set de Direction y esta clase hace el
 * trabajo de "deslizar hasta chocar" una sola vez, para las tres piezas.
 */
public final class SlidingMovement {

    private SlidingMovement() {
    }

    public static List<Position> getPositions(Position from, Board board, PieceColor color, List<Direction> directions) {
        List<Position> positions = new ArrayList<>();
        for (Direction direction : directions) {
            Position current = from.translate(direction);
            while (current.isWithin(board.getDimensions())) {
                if (board.isEmpty(current)) {
                    positions.add(current);
                } else {
                    if (board.isOccupiedByEnemy(current, color)) {
                        positions.add(current); // puede capturar, pero no seguir más allá
                    }
                    break; // bloqueado, sea pieza propia o rival
                }
                current = current.translate(direction);
            }
        }
        return positions;
    }
}
