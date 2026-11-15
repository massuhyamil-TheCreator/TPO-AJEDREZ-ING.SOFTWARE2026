package com.uade.chess.domain.pieces;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;

import java.util.List;

/**
 * Contrato común a las 6 piezas. MoveValidator y CheckDetector trabajan
 * siempre contra esta abstracción — nunca hacen "if (piece instanceof Rook)".
 * Eso es lo que permite agregar una pieza nueva sin tocar ninguna clase
 * existente (Open/Closed).
 */
public abstract class Piece {
    private final PieceColor color;

    protected Piece(PieceColor color) {
        this.color = color;
    }

    public PieceColor getColor() {
        return color;
    }

    /** Casillas a las que esta pieza se puede MOVER desde "from", dado el estado del tablero. */
    public abstract List<Position> getReachablePositions(Position from, Board board);

    /**
     * Casillas que esta pieza AMENAZA (usado para detección de jaque).
     * Por defecto es igual a getReachablePositions — el Peón es el único
     * caso especial (avanza derecho, pero amenaza en diagonal).
     */
    public List<Position> getThreatenedPositions(Position from, Board board) {
        return getReachablePositions(from, board);
    }

    public abstract char getSymbol();
}
