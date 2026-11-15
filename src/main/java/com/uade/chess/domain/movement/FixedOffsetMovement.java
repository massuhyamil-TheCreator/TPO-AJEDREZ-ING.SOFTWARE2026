package com.uade.chess.domain.movement;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.Direction;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Lógica de movimiento de un solo paso, compartida por Caballo y Rey
 * mediante composición de offsets — misma idea que SlidingMovement pero
 * sin repetir la dirección hasta chocar, solo un paso fijo.
 */
public final class FixedOffsetMovement {

    private FixedOffsetMovement() {
    }

    public static List<Position> getPositions(Position from, Board board, PieceColor color, List<Direction> offsets) {
        List<Position> positions = new ArrayList<>();
        for (Direction offset : offsets) {
            Position target = from.translate(offset);
            if (!target.isWithin(board.getDimensions())) {
                continue;
            }
            if (board.isEmpty(target) || board.isOccupiedByEnemy(target, color)) {
                positions.add(target);
            }
        }
        return positions;
    }
}
