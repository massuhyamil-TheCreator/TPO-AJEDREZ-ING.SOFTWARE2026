package com.uade.chess.domain;

public enum MoveFailureReason {
    OUT_OF_BOUNDS,
    EMPTY_ORIGIN,
    NOT_YOUR_TURN,
    FRIENDLY_FIRE,
    INVALID_PIECE_MOVEMENT,
    SELF_CHECK
}
