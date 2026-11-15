package com.uade.chess.application;

import com.uade.chess.domain.MoveResult;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChessMatchTest {

    @Test
    void newMatchStartsWithWhiteTurn() {
        ChessGame game = ChessMatch.newStandardMatch();

        assertEquals(PieceColor.WHITE, game.getCurrentTurn());
    }

    @Test
    void successfulMoveSwitchesTurn() {
        ChessGame game = ChessMatch.newStandardMatch();

        MoveResult result = game.move(new Position(1, 4), new Position(3, 4)); // e2-e4

        assertTrue(result.isSuccess());
        assertEquals(PieceColor.BLACK, game.getCurrentTurn());
    }

    @Test
    void failedMoveDoesNotSwitchTurn() {
        ChessGame game = ChessMatch.newStandardMatch();

        MoveResult result = game.move(new Position(3, 4), new Position(4, 4)); // casilla vacía

        assertFalse(result.isSuccess());
        assertEquals(PieceColor.WHITE, game.getCurrentTurn());
    }

    @Test
    void scholarsMateOpeningEndsInCheck() {
        ChessGame game = ChessMatch.newStandardMatch();

        assertTrue(game.move(new Position(1, 4), new Position(3, 4)).isSuccess()); // 1. e4
        assertTrue(game.move(new Position(6, 4), new Position(4, 4)).isSuccess()); // 1... e5
        assertTrue(game.move(new Position(0, 5), new Position(3, 2)).isSuccess()); // 2. Bc4
        assertTrue(game.move(new Position(7, 1), new Position(5, 2)).isSuccess()); // 2... Nc6
        assertTrue(game.move(new Position(0, 3), new Position(4, 7)).isSuccess()); // 3. Qh5
        assertTrue(game.move(new Position(6, 0), new Position(5, 0)).isSuccess()); // 3... a6 (no defiende)

        MoveResult scholarsMate = game.move(new Position(4, 7), new Position(6, 5)); // 4. Qxf7+

        assertTrue(scholarsMate.isSuccess());
        assertTrue(scholarsMate.isCheck());
        assertEquals(GameStatus.CHECK, game.getStatus());
    }
}
