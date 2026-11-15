package com.uade.chess.domain.pieces;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Caso especial de verdad: no comparte SlidingMovement ni FixedOffsetMovement
 * porque su regla es única (avanza derecho solo si está vacío, 2 casillas
 * desde la fila inicial; amenaza/captura en diagonal, nunca derecho).
 * Por eso getReachablePositions y getThreatenedPositions difieren acá,
 * a diferencia de todas las demás piezas que heredan el comportamiento
 * por defecto de Piece.
 */
public final class Pawn extends Piece {

    public Pawn(PieceColor color) {
        super(color);
    }

    private int forwardStep() {
        return getColor() == PieceColor.WHITE ? 1 : -1;
    }

    private boolean isOnStartingRow(Position from, Board board) {
        int startingRow = getColor() == PieceColor.WHITE ? 1 : board.getDimensions().height() - 2;
        return from.row() == startingRow;
    }

    @Override
    public List<Position> getReachablePositions(Position from, Board board) {
        List<Position> positions = new ArrayList<>();
        int step = forwardStep();

        Position oneForward = new Position(from.row() + step, from.column());
        if (oneForward.isWithin(board.getDimensions()) && board.isEmpty(oneForward)) {
            positions.add(oneForward);

            Position twoForward = new Position(from.row() + step * 2, from.column());
            if (isOnStartingRow(from, board)
                    && twoForward.isWithin(board.getDimensions())
                    && board.isEmpty(twoForward)) {
                positions.add(twoForward);
            }
        }

        for (Position diagonal : diagonalTargets(from, step)) {
            if (diagonal.isWithin(board.getDimensions()) && board.isOccupiedByEnemy(diagonal, getColor())) {
                positions.add(diagonal);
            }
        }

        return positions;
    }

    @Override
    public List<Position> getThreatenedPositions(Position from, Board board) {
        List<Position> threatened = new ArrayList<>();
        for (Position diagonal : diagonalTargets(from, forwardStep())) {
            if (diagonal.isWithin(board.getDimensions())) {
                threatened.add(diagonal);
            }
        }
        return threatened;
    }

    private List<Position> diagonalTargets(Position from, int step) {
        return List.of(
                new Position(from.row() + step, from.column() - 1),
                new Position(from.row() + step, from.column() + 1)
        );
    }

    @Override
    public char getSymbol() {
        return getColor() == PieceColor.WHITE ? 'P' : 'p';
    }
}
