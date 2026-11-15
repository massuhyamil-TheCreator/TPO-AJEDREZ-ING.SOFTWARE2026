package com.uade.chess.gui;

import com.uade.chess.application.ChessGame;
import com.uade.chess.domain.Move;
import com.uade.chess.domain.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Consulta visual mediante partidas descartables. Usa solo ChessGame:
 * no lee Board, no invoca MoveValidator, no duplica reglas de ajedrez.
 * La fábrica debe devolver una partida NUEVA con el mismo estado inicial
 * en cada llamada. Se reproduce únicamente el historial de jugadas aceptadas.
 */
public final class LegalMovePreview {
    private final Supplier<ChessGame> newGame;

    public LegalMovePreview(Supplier<ChessGame> newGame) {
        this.newGame = Objects.requireNonNull(newGame);
    }

    public List<Position> destinations(Position origin, List<Move> history) {
        List<Position> destinations = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                Position target = new Position(row, column);
                if (target.equals(origin)) continue;
                ChessGame simulation = newGame.get();
                for (Move move : history) {
                    if (!simulation.move(move.from(), move.to()).isSuccess()) {
                        throw new IllegalStateException("La fábrica no reproduce la partida inicial.");
                    }
                }
                if (simulation.move(origin, target).isSuccess()) destinations.add(target);
            }
        }
        return List.copyOf(destinations);
    }
}
