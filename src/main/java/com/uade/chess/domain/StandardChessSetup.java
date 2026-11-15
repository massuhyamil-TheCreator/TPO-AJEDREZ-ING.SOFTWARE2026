package com.uade.chess.domain;

import com.uade.chess.domain.pieces.Bishop;
import com.uade.chess.domain.pieces.King;
import com.uade.chess.domain.pieces.Knight;
import com.uade.chess.domain.pieces.Pawn;
import com.uade.chess.domain.pieces.Queen;
import com.uade.chess.domain.pieces.Rook;

/**
 * Arma la posición inicial de 32 piezas sobre un Board vacío.
 * Separado de Board a propósito: Board no debe saber nada sobre "cuál es
 * la posición inicial estándar de ajedrez" — eso es una decisión de más
 * arriba, no una responsabilidad del estado del tablero en sí.
 */
public final class StandardChessSetup {

    private StandardChessSetup() {
    }

    public static Board createInitialBoard() {
        return createInitialBoard(BoardDimensions.STANDARD);
    }

    public static Board createInitialBoard(BoardDimensions dimensions) {
        Board board = new Board(dimensions);

        placeBackRank(board, 0, PieceColor.WHITE);
        placePawns(board, 1, PieceColor.WHITE);

        placeBackRank(board, dimensions.height() - 1, PieceColor.BLACK);
        placePawns(board, dimensions.height() - 2, PieceColor.BLACK);

        return board;
    }

    private static void placeBackRank(Board board, int row, PieceColor color) {
        board.placePiece(new Position(row, 0), new Rook(color));
        board.placePiece(new Position(row, 1), new Knight(color));
        board.placePiece(new Position(row, 2), new Bishop(color));
        board.placePiece(new Position(row, 3), new Queen(color));
        board.placePiece(new Position(row, 4), new King(color));
        board.placePiece(new Position(row, 5), new Bishop(color));
        board.placePiece(new Position(row, 6), new Knight(color));
        board.placePiece(new Position(row, 7), new Rook(color));
    }

    private static void placePawns(Board board, int row, PieceColor color) {
        for (int column = 0; column < 8; column++) {
            board.placePiece(new Position(row, column), new Pawn(color));
        }
    }
}
