package com.uade.chess.domain;

import com.uade.chess.domain.pieces.King;
import com.uade.chess.domain.pieces.Piece;

import java.util.HashMap;
import java.util.Map;

/**
 * Estado actual del tablero: qué pieza hay en cada casilla. A propósito
 * NO sabe nada de reglas de validez de movimiento — eso vive en
 * MoveValidator/CheckDetector. Board solo administra el estado.
 */
public final class Board {
    private final BoardDimensions dimensions;
    private final Map<Position, Piece> squares;

    public Board(BoardDimensions dimensions) {
        this.dimensions = dimensions;
        this.squares = new HashMap<>();
    }

    private Board(BoardDimensions dimensions, Map<Position, Piece> squares) {
        this.dimensions = dimensions;
        this.squares = squares;
    }

    public BoardDimensions getDimensions() {
        return dimensions;
    }

    public Piece getPieceAt(Position position) {
        return squares.get(position);
    }

    public boolean isEmpty(Position position) {
        return !squares.containsKey(position);
    }

    public boolean isOccupiedByEnemy(Position position, PieceColor color) {
        Piece piece = squares.get(position);
        return piece != null && piece.getColor() != color;
    }

    public boolean isOccupiedByAlly(Position position, PieceColor color) {
        Piece piece = squares.get(position);
        return piece != null && piece.getColor() == color;
    }

    public void placePiece(Position position, Piece piece) {
        squares.put(position, piece);
    }

    public void removePiece(Position position) {
        squares.remove(position);
    }

    /** Mueve la pieza de "from" a "to". Si "to" tenía una pieza, queda capturada (sobrescrita). */
    public void movePiece(Position from, Position to) {
        Piece piece = squares.remove(from);
        if (piece == null) {
            throw new IllegalStateException("No hay pieza en " + from);
        }
        squares.put(to, piece);
    }

    public Position findKing(PieceColor color) {
        for (Map.Entry<Position, Piece> entry : squares.entrySet()) {
            if (entry.getValue() instanceof King && entry.getValue().getColor() == color) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("No se encontró el rey de " + color);
    }

    public Map<Position, Piece> getOccupiedSquares() {
        return Map.copyOf(squares);
    }

    /**
     * Snapshot del tablero, usado por MoveValidator para simular un movimiento
     * y verificar self-check sin tocar el tablero real. Las piezas en sí son
     * inmutables (su único campo es el color), así que alcanza con copiar el
     * mapa de posiciones — no hace falta clonar cada Piece individualmente.
     */
    public Board clone() {
        return new Board(dimensions, new HashMap<>(squares));
    }
}
