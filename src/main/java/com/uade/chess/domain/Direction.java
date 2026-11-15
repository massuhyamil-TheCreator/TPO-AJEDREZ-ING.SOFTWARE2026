package com.uade.chess.domain;

import java.util.List;

/**
 * Vector de desplazamiento (deltaRow, deltaColumn). Se reutiliza tanto para
 * piezas deslizantes (Torre/Alfil/Reina, vía SlidingMovement) como para
 * piezas de salto fijo (Caballo/Rey, vía FixedOffsetMovement) — la misma
 * abstracción sirve para los dos casos por composición, sin duplicar código.
 */
public record Direction(int deltaRow, int deltaColumn) {

    public static final Direction UP = new Direction(1, 0);
    public static final Direction DOWN = new Direction(-1, 0);
    public static final Direction LEFT = new Direction(0, -1);
    public static final Direction RIGHT = new Direction(0, 1);
    public static final Direction UP_LEFT = new Direction(1, -1);
    public static final Direction UP_RIGHT = new Direction(1, 1);
    public static final Direction DOWN_LEFT = new Direction(-1, -1);
    public static final Direction DOWN_RIGHT = new Direction(-1, 1);

    public static final List<Direction> ORTHOGONAL = List.of(UP, DOWN, LEFT, RIGHT);
    public static final List<Direction> DIAGONAL = List.of(UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT);
    public static final List<Direction> ALL_EIGHT =
            List.of(UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT);

    public static final List<Direction> KNIGHT_OFFSETS = List.of(
            new Direction(2, 1), new Direction(2, -1),
            new Direction(-2, 1), new Direction(-2, -1),
            new Direction(1, 2), new Direction(1, -2),
            new Direction(-1, 2), new Direction(-1, -2)
    );
}
