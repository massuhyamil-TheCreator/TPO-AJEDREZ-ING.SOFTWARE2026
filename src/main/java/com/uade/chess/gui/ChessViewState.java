package com.uade.chess.gui;

import com.uade.chess.application.GameStatus;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import com.uade.chess.domain.pieces.Piece;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Instantánea para la vista. El tablero conserva null para las casillas vacías. */
public record ChessViewState(List<Piece> board, PieceColor turn, GameStatus status,
                             Position selected, List<Position> legalMoves,
                             List<MoveEntry> history, String message, boolean error) {
    public ChessViewState {
        board = Collections.unmodifiableList(new ArrayList<>(board));
        legalMoves = List.copyOf(legalMoves);
        history = List.copyOf(history);
    }

    public Piece pieceAt(Position position) {
        return board.get(position.row() * 8 + position.column());
    }

    public MoveEntry lastMove() {
        return history.isEmpty() ? null : history.get(history.size() - 1);
    }

    public List<Piece> capturedBy(PieceColor player) {
        return history.stream().filter(entry -> entry.player() == player)
                .map(MoveEntry::capturedPiece).filter(piece -> piece != null).toList();
    }
}
