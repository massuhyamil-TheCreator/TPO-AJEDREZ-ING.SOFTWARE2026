package com.uade.chess.domain.pieces;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.Direction;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import com.uade.chess.domain.movement.SlidingMovement;

import java.util.List;

public final class Rook extends Piece {

    public Rook(PieceColor color) {
        super(color);
    }

    @Override
    public List<Position> getReachablePositions(Position from, Board board) {
        return SlidingMovement.getPositions(from, board, getColor(), Direction.ORTHOGONAL);
    }

    @Override
    public char getSymbol() {
        return getColor() == PieceColor.WHITE ? 'R' : 'r';
    }
}
