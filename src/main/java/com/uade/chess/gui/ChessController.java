package com.uade.chess.gui;

import com.uade.chess.application.ChessGame;
import com.uade.chess.application.GameStatus;
import com.uade.chess.domain.Move;
import com.uade.chess.domain.MoveResult;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import com.uade.chess.domain.pieces.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/** Estado de interacción; todas las reglas y mutaciones se delegan a ChessGame. */
public final class ChessController {
    private final Supplier<ChessGame> newGame;
    private final LegalMovePreview preview;
    private ChessGame game;
    private final List<MoveEntry> history = new ArrayList<>();
    private Position selected;
    private List<Position> legalMoves = List.of();
    private String message = "Elegí una pieza blanca para empezar.";
    private boolean error;

    public ChessController(Supplier<ChessGame> newGame) {
        this.newGame = Objects.requireNonNull(newGame);
        this.preview = new LegalMovePreview(newGame);
        this.game = newGame.get();
    }

    public void click(Position position) {
        Objects.requireNonNull(position);
        if (position.row() < 0 || position.row() >= 8
                || position.column() < 0 || position.column() >= 8) return;
        if (isGameOver()) {
            message = "La partida terminó. Elegí \"Nueva partida\" para seguir jugando.";
            error = true;
            return;
        }
        Piece target = game.getPieceAt(position);
        if (position.equals(selected)) {
            clearSelection();
            message = "Selección cancelada. Elegí otra pieza.";
            error = false;
        } else if (target != null && target.getColor() == game.getCurrentTurn()) {
            selected = position;
            legalMoves = preview.destinations(position, history.stream().map(MoveEntry::move).toList());
            message = PresentationText.piece(target) + " en " + position + ". "
                    + (legalMoves.isEmpty() ? "No tiene movimientos legales." : "Elegí una casilla de destino.");
            error = false;
        } else if (selected == null) {
            message = target == null ? "Elegí primero una pieza de "
                    + PresentationText.color(game.getCurrentTurn()).toLowerCase() + "."
                    : "Es el turno de " + PresentationText.color(game.getCurrentTurn()).toLowerCase() + ".";
            error = true;
        } else {
            Piece movedPiece = game.getPieceAt(selected);
            PieceColor player = game.getCurrentTurn();
            MoveResult result = game.move(selected, position);
            if (result.isSuccess()) {
                MoveEntry entry = new MoveEntry(player, movedPiece, new Move(selected, position),
                        result.getCapturedPiece(), result.isCheck());
                history.add(entry);
                clearSelection();
                message = switch (game.getStatus()) {
                    case CHECKMATE -> entry.notation() + " · ¡Jaque mate! Ganan las "
                            + PresentationText.color(player).toLowerCase() + ".";
                    case STALEMATE -> entry.notation() + " · Ahogado, tablas sin movimientos legales.";
                    case CHECK -> entry.notation() + " · ¡Jaque!";
                    case IN_PROGRESS -> entry.notation() + " · Movimiento realizado.";
                };
                error = false;
            } else {
                message = PresentationText.failure(result.getFailureReason());
                error = true;
            }
        }
    }

    public boolean isGameOver() {
        GameStatus status = game.getStatus();
        return status == GameStatus.CHECKMATE || status == GameStatus.STALEMATE;
    }

    public void clearSelection() {
        selected = null;
        legalMoves = List.of();
    }

    public void cancelSelection() {
        clearSelection();
        message = "Elegí una pieza para continuar.";
        error = false;
    }

    public void newMatch() {
        game = newGame.get();
        history.clear();
        clearSelection();
        message = "Nueva partida. Empiezan las blancas.";
        error = false;
    }

    public ChessViewState state() {
        List<Piece> board = new ArrayList<>(64);
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                board.add(game.getPieceAt(new Position(row, column)));
            }
        }
        return new ChessViewState(board, game.getCurrentTurn(), game.getStatus(), selected,
                legalMoves, history, message, error);
    }
}
