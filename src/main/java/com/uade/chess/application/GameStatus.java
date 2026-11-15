package com.uade.chess.application;

/**
 * Deliberadamente solo 2 valores por ahora: cubre el alcance obligatorio
 * (detectar jaque). CHECKMATE y STALEMATE quedan para la fase opcional
 * del TPO, cuando se evalúe si de verdad hace falta más que esto.
 */
public enum GameStatus {
    IN_PROGRESS,
    CHECK
}
