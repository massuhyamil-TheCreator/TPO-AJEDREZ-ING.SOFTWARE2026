package com.uade.chess.domain;

/** Intención de mover una pieza de una casilla a otra. No sabe si es válido. */
public record Move(Position from, Position to) {
}
