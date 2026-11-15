package com.uade.chess.gui;

/**
 * Punto de entrada real. No extiende Application a propósito: así Java no
 * activa el chequeo especial de "módulos de JavaFX faltantes" que rompe
 * cuando JavaFX viene por classpath (Maven) en vez de module-path.
 */
public final class Launcher {
    public static void main(String[] args) {
        ChessGuiApp.main(args);
    }
}