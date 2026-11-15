package com.uade.chess.domain.pieces;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.Direction;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import com.uade.chess.domain.movement.SlidingMovement;

import java.util.List;

public final class Queen extends Piece {

    public Queen(PieceColor color) {
        super(color);
    }

    @Override
    public List<Position> getReachablePositions(Position from, Board board) {
        return SlidingMovement.getPositions(from, board, getColor(), Direction.ALL_EIGHT);
    }

    @Override
    public char getSymbol() {
        return getColor() == PieceColor.WHITE ? 'Q' : 'q';
    }
}
