package com.uade.chess.application;

import com.uade.chess.domain.MoveResult;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import com.uade.chess.domain.pieces.Piece;

/**
 * Puerto de entrada (driving port): el único contrato que cualquier
 * adaptador (consola, una UI futura, o los tests) usa para jugar.
 * Chess.Console depende de ESTA interfaz, nunca de ChessMatch directamente
 * (Dependency Inversion / Nivel 4 de inyección de dependencias).
 */
public interface ChessGame {
    MoveResult move(Position from, Position to);

    Piece getPieceAt(Position position);

    PieceColor getCurrentTurn();

    GameStatus getStatus();
}
