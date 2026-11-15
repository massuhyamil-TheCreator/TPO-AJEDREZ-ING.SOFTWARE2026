package com.uade.chess.gui;

import com.uade.chess.application.ChessMatch;
import javafx.application.Application;
import javafx.stage.Stage;

/** Composition root gráfico: el único lugar del adaptador que conoce ChessMatch. */
public final class ChessGuiApp extends Application {
    @Override
    public void start(Stage stage) {
        ChessController controller = new ChessController(ChessMatch::newStandardMatch);
        new ChessWindow(stage, controller).show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
