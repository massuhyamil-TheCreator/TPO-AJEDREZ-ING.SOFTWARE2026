package com.uade.chess.domain.pieces;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.BoardDimensions;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RookTest {

    @Test
    void rookMovesFreelyOnEmptyBoard() {
        Board board = new Board(BoardDimensions.STANDARD);
        Position from = new Position(3, 3);
        Rook rook = new Rook(PieceColor.WHITE);
        board.placePiece(from, rook);

        List<Position> reachable = rook.getReachablePositions(from, board);

        assertTrue(reachable.contains(new Position(0, 3)));
        assertTrue(reachable.contains(new Position(7, 3)));
        assertTrue(reachable.contains(new Position(3, 0)));
        assertTrue(reachable.contains(new Position(3, 7)));
        assertFalse(reachable.contains(new Position(4, 4))); // diagonal, no es movimiento de torre
    }

    @Test
    void rookStopsBeforeFriendlyPiece() {
        Board board = new Board(BoardDimensions.STANDARD);
        Position from = new Position(0, 0);
        board.placePiece(from, new Rook(PieceColor.WHITE));
        board.placePiece(new Position(3, 0), new Rook(PieceColor.WHITE));

        List<Position> reachable = board.getPieceAt(from).getReachablePositions(from, board);

        assertFalse(reachable.contains(new Position(3, 0)));
        assertFalse(reachable.contains(new Position(4, 0)));
        assertTrue(reachable.contains(new Position(2, 0)));
    }

    @Test
    void rookCanCaptureEnemyButNotJumpOverIt() {
        Board board = new Board(BoardDimensions.STANDARD);
        Position from = new Position(0, 0);
        board.placePiece(from, new Rook(PieceColor.WHITE));
        board.placePiece(new Position(3, 0), new Rook(PieceColor.BLACK));

        List<Position> reachable = board.getPieceAt(from).getReachablePositions(from, board);

        assertTrue(reachable.contains(new Position(3, 0)));
        assertFalse(reachable.contains(new Position(4, 0)));
    }
}
