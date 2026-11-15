package com.uade.chess.domain;

/**
 * Tamaño del tablero, centralizado acá para que ningún "8" quede
 * hardcodeado suelto en el código de reglas. Cambiar el tamaño del
 * tablero es, en el caso ideal, cambiar solo este valor.
 */
public record BoardDimensions(int width, int height) {

    public static final BoardDimensions STANDARD = new BoardDimensions(8, 8);
}
