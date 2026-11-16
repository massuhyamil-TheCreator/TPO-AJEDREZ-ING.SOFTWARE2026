package com.uade.chess.application;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.BoardDimensions;
import com.uade.chess.domain.MoveResult;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import com.uade.chess.domain.pieces.King;
import com.uade.chess.domain.pieces.Queen;
import com.uade.chess.domain.rules.CheckDetector;
import com.uade.chess.domain.rules.MoveValidator;
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
    void scholarsMateEndsInCheckmate() {
        // Variante del Mate del Aficionado donde Negras no defienden f7
        // (3...a6 en vez de 3...Qe7/g6): 4. Qxf7 es jaque mate real, no
        // solo jaque -- el rey no tiene ninguna casilla de escape (d8 y f8
        // están ocupadas por piezas propias, d7/e7 están cubiertas por la
        // dama en f7) y nada puede capturarla ni bloquear (la defiende el
        // alfil en c4). Antes del fix, getStatus() se quedaba en CHECK para
        // siempre, aunque Blancas ya habían ganado la partida.
        ChessGame game = ChessMatch.newStandardMatch();

        assertTrue(game.move(new Position(1, 4), new Position(3, 4)).isSuccess()); // 1. e4
        assertTrue(game.move(new Position(6, 4), new Position(4, 4)).isSuccess()); // 1... e5
        assertTrue(game.move(new Position(0, 5), new Position(3, 2)).isSuccess()); // 2. Bc4
        assertTrue(game.move(new Position(7, 1), new Position(5, 2)).isSuccess()); // 2... Nc6
        assertTrue(game.move(new Position(0, 3), new Position(4, 7)).isSuccess()); // 3. Qh5
        assertTrue(game.move(new Position(6, 0), new Position(5, 0)).isSuccess()); // 3... a6 (no defiende)

        MoveResult scholarsMate = game.move(new Position(4, 7), new Position(6, 5)); // 4. Qxf7#

        assertTrue(scholarsMate.isSuccess());
        assertTrue(scholarsMate.isCheck());
        assertEquals(GameStatus.CHECKMATE, game.getStatus());
        assertEquals(PieceColor.BLACK, game.getCurrentTurn()); // a quien le toca mover y no puede
    }

    @Test
    void foolsMateIsCheckmateInTwoMoves() {
        // El jaque mate más rápido posible (2 jugadas por lado): 1. f3 e5
        // 2. g4 Qh4#. Reproduce exactamente el bug reportado: jugando esta
        // partida, Blancas se queda sin ningún movimiento legal y antes
        // del fix el motor nunca lo informaba.
        ChessGame game = ChessMatch.newStandardMatch();

        assertTrue(game.move(new Position(1, 5), new Position(2, 5)).isSuccess()); // 1. f3
        assertTrue(game.move(new Position(6, 4), new Position(4, 4)).isSuccess()); // 1... e5
        assertTrue(game.move(new Position(1, 6), new Position(3, 6)).isSuccess()); // 2. g4
        MoveResult foolsMate = game.move(new Position(7, 3), new Position(3, 7)); // 2... Qh4#

        assertTrue(foolsMate.isSuccess());
        assertTrue(foolsMate.isCheck());
        assertEquals(GameStatus.CHECKMATE, game.getStatus());
    }

    @Test
    void checkWithALegalEscapeIsNotCheckmate() {
        // Control negativo: un jaque que SÍ tiene respuesta legal (p.ej.
        // bloquear con un peón) nunca debe reportarse como CHECKMATE.
        // Sin este test, una implementación de hasAnyLegalMove() demasiado
        // agresiva podría "ver" jaque mate donde en realidad hay salida.
        ChessGame game = ChessMatch.newStandardMatch();

        assertTrue(game.move(new Position(1, 4), new Position(3, 4)).isSuccess()); // 1. e4
        assertTrue(game.move(new Position(6, 3), new Position(4, 3)).isSuccess()); // 1... d5
        MoveResult bishopCheck = game.move(new Position(0, 5), new Position(4, 1)); // 2. Bb5+

        assertTrue(bishopCheck.isSuccess());
        assertTrue(bishopCheck.isCheck());
        assertEquals(GameStatus.CHECK, game.getStatus()); // no CHECKMATE: ...c6 bloquea
    }

    @Test
    void stalemateWithNoLegalMovesIsDetectedWithoutCheck() {
        // Ahogado clásico de "Rey+Dama vs Rey": Blancas (Rey solo en h1) no
        // están en jaque, pero cualquier casilla a la que se mueva su rey
        // está cubierta por el rey o la dama negra. Antes del fix esto se
        // reportaba como IN_PROGRESS, como si la partida pudiera seguir.
        Board board = new Board(BoardDimensions.STANDARD);
        board.placePiece(new Position(0, 7), new King(PieceColor.WHITE));  // h1
        board.placePiece(new Position(1, 5), new King(PieceColor.BLACK));  // f2
        board.placePiece(new Position(2, 6), new Queen(PieceColor.BLACK)); // g3

        CheckDetector checkDetector = new CheckDetector();
        MoveValidator moveValidator = new MoveValidator(checkDetector);
        ChessGame game = new ChessMatch(board, moveValidator, checkDetector);

        assertEquals(PieceColor.WHITE, game.getCurrentTurn());
        assertEquals(GameStatus.STALEMATE, game.getStatus());
    }
}
