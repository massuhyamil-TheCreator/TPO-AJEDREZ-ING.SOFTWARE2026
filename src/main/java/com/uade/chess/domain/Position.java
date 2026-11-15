package com.uade.chess.domain;

/**
 * Casilla del tablero. Inmutable, con igualdad por valor (record de Java).
 * row/column arrancan en 0: row 0 = fila 1 (lado blanco), column 0 = columna 'a'.
 */
public record Position(int row, int column) {

    public boolean isWithin(BoardDimensions dimensions) {
        return row >= 0 && row < dimensions.height()
                && column >= 0 && column < dimensions.width();
    }

    public Position translate(Direction direction) {
        return new Position(row + direction.deltaRow(), column + direction.deltaColumn());
    }

    @Override
    public String toString() {
        char file = (char) ('a' + column);
        int rank = row + 1;
        return "" + file + rank;
    }
}
