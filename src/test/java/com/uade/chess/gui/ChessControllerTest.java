package com.uade.chess.gui;

import com.uade.chess.application.ChessMatch;
import com.uade.chess.application.GameStatus;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Integración del adaptador: se prueba sin abrir JavaFX ni requerir una pantalla. */
class ChessControllerTest {
    private final ChessController controller = new ChessController(ChessMatch::newStandardMatch);

    private Position position(String square) {
        return new Position(square.charAt(1) - '1', square.charAt(0) - 'a');
    }

    private void play(String from, String to) {
        controller.click(position(from));
        controller.click(position(to));
        assertFalse(controller.state().error(), controller.state().message());
    }

    @Test
    void previewsDoNotMutateTheLiveMatch() {
        var before = controller.state();
        controller.click(position("e2"));
        var after = controller.state();
        assertEquals(List.of(position("e3"), position("e4")), after.legalMoves());
        assertEquals(before.board(), after.board());
        assertEquals(PieceColor.WHITE, after.turn());
        assertTrue(after.history().isEmpty());
    }

    @Test
    void invalidMoveKeepsSelectionBoardAndTurn() {
        var before = controller.state().board();
        controller.click(position("e2"));
        controller.click(position("e5"));
        var state = controller.state();
        assertTrue(state.error());
        assertEquals(position("e2"), state.selected());
        assertEquals(before, state.board());
        assertEquals(PieceColor.WHITE, state.turn());
        assertTrue(state.history().isEmpty());
    }

    @Test
    void ownPieceReselectsAndSecondClickCancels() {
        controller.click(position("e2"));
        controller.click(position("d2"));
        assertEquals(position("d2"), controller.state().selected());
        assertEquals(List.of(position("d3"), position("d4")), controller.state().legalMoves());
        controller.click(position("d2"));
        assertNull(controller.state().selected());
        assertTrue(controller.state().legalMoves().isEmpty());
        assertTrue(controller.state().history().isEmpty());
    }

    @Test
    void opponentAndEmptySquareCannotStartSelection() {
        controller.click(position("e7"));
        assertTrue(controller.state().error());
        assertNull(controller.state().selected());
        controller.click(position("e5"));
        assertNull(controller.state().selected());
        assertEquals(PieceColor.WHITE, controller.state().turn());
    }

    @Test
    void acceptedMoveUpdatesTurnAndHistory() {
        play("e2", "e4");
        var state = controller.state();
        assertNull(state.pieceAt(position("e2")));
        assertEquals('P', state.pieceAt(position("e4")).getSymbol());
        assertEquals(PieceColor.BLACK, state.turn());
        assertNull(state.selected());
        assertEquals("e2 → e4", state.lastMove().notation());
        assertEquals(1, state.history().size());
    }

    @Test
    void capturesAreAttributedToTheMovingPlayer() {
        play("e2", "e4");
        play("d7", "d5");
        play("e4", "d5");
        var state = controller.state();
        assertEquals(1, state.capturedBy(PieceColor.WHITE).size());
        assertEquals('p', state.capturedBy(PieceColor.WHITE).get(0).getSymbol());
        assertTrue(state.capturedBy(PieceColor.BLACK).isEmpty());
        assertEquals("e4 × d5", state.lastMove().notation());
        assertNull(state.pieceAt(position("e4")));
    }

    @Test
    void resetClearsHistoryCapturesSelectionAndRestoresBoard() {
        play("e2", "e4");
        play("d7", "d5");
        play("e4", "d5");
        controller.click(position("g8"));
        controller.newMatch();
        var state = controller.state();
        assertEquals(PieceColor.WHITE, state.turn());
        assertTrue(state.history().isEmpty());
        assertTrue(state.capturedBy(PieceColor.WHITE).isEmpty());
        assertNull(state.selected());
        assertTrue(state.legalMoves().isEmpty());
        assertEquals('P', state.pieceAt(position("e2")).getSymbol());
        assertEquals('p', state.pieceAt(position("d7")).getSymbol());
    }

    @Test
    void legalHintsExcludeSelfCheckAfterReplayingHistory() {
        play("e2", "e4");
        play("d7", "d5");
        play("f1", "b5");
        play("b8", "c6");
        play("a2", "a3");
        controller.click(position("c6"));
        assertFalse(controller.state().legalMoves().contains(position("b4")));
        controller.click(position("b4"));
        assertTrue(controller.state().error());
        assertEquals("Ese movimiento dejaría a tu rey en jaque.", controller.state().message());
        assertEquals(PieceColor.BLACK, controller.state().turn());
        assertEquals(5, controller.state().history().size());
    }

    @Test
    void checkComesFromTheBackendAndIsStoredInHistory() {
        play("e2", "e4");
        play("e7", "e5");
        play("f1", "c4");
        play("b8", "c6");
        play("d1", "h5");
        play("a7", "a6");
        play("h5", "f7");
        var state = controller.state();
        assertEquals(GameStatus.CHECK, state.status());
        assertTrue(state.lastMove().check());
        assertEquals("h5 × f7 +", state.lastMove().notation());
        assertEquals(PieceColor.BLACK, state.turn());
    }

    @Test
    void snapshotsKeepTheirPreviousStateAndCannotBeEdited() {
        var before = controller.state();
        play("e2", "e4");
        assertTrue(before.history().isEmpty());
        assertNotNull(before.pieceAt(position("e2")));
        assertThrows(UnsupportedOperationException.class, () -> before.board().clear());
        assertThrows(UnsupportedOperationException.class, () -> controller.state().history().clear());
    }
}
