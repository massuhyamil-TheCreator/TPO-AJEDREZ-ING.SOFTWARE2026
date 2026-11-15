package com.uade.chess.domain.pieces;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.Direction;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import com.uade.chess.domain.movement.FixedOffsetMovement;

import java.util.List;

public final class King extends Piece {

    public King(PieceColor color) {
        super(color);
    }

    @Override
    public List<Position> getReachablePositions(Position from, Board board) {
        return FixedOffsetMovement.getPositions(from, board, getColor(), Direction.ALL_EIGHT);
    }

    @Override
    public char getSymbol() {
        return getColor() == PieceColor.WHITE ? 'K' : 'k';
    }
}
