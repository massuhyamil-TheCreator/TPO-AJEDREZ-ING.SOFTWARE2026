package com.uade.chess.console;

import com.uade.chess.domain.Position;

/** Detalle de adaptador: traduce notación algebraica ("e2") a Position. El núcleo no sabe que esto existe. */
public final class PositionParser {

    private PositionParser() {
    }

    public static Position parse(String notation) {
        if (notation == null || notation.length() != 2) {
            throw new IllegalArgumentException("Formato inválido, usá por ejemplo 'e2'");
        }
        char fileChar = Character.toLowerCase(notation.charAt(0));
        char rankChar = notation.charAt(1);

        int column = fileChar - 'a';
        int row = rankChar - '1';

        return new Position(row, column);
    }
}
