package com.uade.chess.console;

import com.uade.chess.application.ChessGame;
import com.uade.chess.domain.Position;
import com.uade.chess.domain.pieces.Piece;

public final class ConsoleBoardRenderer {

    public void render(ChessGame game) {
        for (int row = 7; row >= 0; row--) {
            StringBuilder line = new StringBuilder();
            line.append(row + 1).append(" ");
            for (int column = 0; column < 8; column++) {
                Piece piece = game.getPieceAt(new Position(row, column));
                line.append(piece == null ? "." : piece.getSymbol()).append(" ");
            }
            System.out.println(line);
        }
        System.out.println("  a b c d e f g h");
    }
}
