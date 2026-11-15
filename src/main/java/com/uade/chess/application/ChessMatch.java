package com.uade.chess.application;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.Move;
import com.uade.chess.domain.MoveResult;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import com.uade.chess.domain.StandardChessSetup;
import com.uade.chess.domain.pieces.Piece;
import com.uade.chess.domain.rules.CheckDetector;
import com.uade.chess.domain.rules.MoveValidator;

/**
 * Implementa IChessGame. Orquesta MoveValidator y CheckDetector sobre un
 * Board — a propósito NO contiene ninguna regla de ajedrez propia, solo
 * coordina en qué orden se llaman las piezas del núcleo. Esto es lo que
 * la consigna pide evitar como "clase gigante ChessGame/Manager": acá
 * ChessMatch es finito porque delega todo el trabajo real.
 */
public final class ChessMatch implements ChessGame {

    private final Board board;
    private final MoveValidator moveValidator;
    private final CheckDetector checkDetector;
    private PieceColor currentTurn;

    public ChessMatch(Board board, MoveValidator moveValidator, CheckDetector checkDetector) {
        this.board = board;
        this.moveValidator = moveValidator;
        this.checkDetector = checkDetector;
        this.currentTurn = PieceColor.WHITE;
    }

    public static ChessMatch newStandardMatch() {
        Board board = StandardChessSetup.createInitialBoard();
        CheckDetector checkDetector = new CheckDetector();
        MoveValidator moveValidator = new MoveValidator(checkDetector);
        return new ChessMatch(board, moveValidator, checkDetector);
    }

    @Override
    public MoveResult move(Position from, Position to) {
        MoveResult result = moveValidator.validate(new Move(from, to), board, currentTurn);
        if (result.isSuccess()) {
            currentTurn = currentTurn.opposite();
        }
        return result;
    }

    @Override
    public Piece getPieceAt(Position position) {
        return board.getPieceAt(position);
    }

    @Override
    public PieceColor getCurrentTurn() {
        return currentTurn;
    }

    @Override
    public GameStatus getStatus() {
        return checkDetector.isInCheck(board, currentTurn) ? GameStatus.CHECK : GameStatus.IN_PROGRESS;
    }
}
