package com.uade.chess.application;

/**
 * IN_PROGRESS/CHECK cubren el alcance obligatorio (detectar jaque).
 * CHECKMATE y STALEMATE son la fase opcional del TPO ("jaque mate" y
 * "tablas por ahogado") — se agregaron porque sin ellos ChessMatch nunca
 * sabía que la partida había terminado: si al jugador en turno no le
 * quedaba ningún movimiento legal, el juego seguía reportando CHECK (o
 * IN_PROGRESS si no estaba en jaque) para siempre, y la Console/GUI
 * simplemente rechazaban cualquier intento sin explicar por qué.
 */
public enum GameStatus {
    IN_PROGRESS,
    CHECK,
    CHECKMATE,
    STALEMATE
}
