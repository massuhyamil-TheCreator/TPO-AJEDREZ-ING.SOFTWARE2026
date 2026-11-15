package com.uade.chess.console;

import com.uade.chess.application.ChessGame;
import com.uade.chess.application.ChessMatch;
import com.uade.chess.domain.MoveResult;
import com.uade.chess.domain.Position;

import java.util.Scanner;

/**
 * Adaptador de consola. Depende de ChessGame (abstracción), nunca de
 * ChessMatch directamente — la instancia concreta se inyecta acá, en el
 * composition root, que es el único lugar del proyecto donde se hace "new"
 * de algo concreto para pasarlo como interfaz (Nivel 4 de DI).
 */
public final class Main {

    public static void main(String[] args) {
        ChessGame game = ChessMatch.newStandardMatch();
        ConsoleBoardRenderer renderer = new ConsoleBoardRenderer();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ajedrez — TPO Ingeniería de Software");
        renderer.render(game);

        while (true) {
            System.out.println("Turno: " + game.getCurrentTurn());
            System.out.print("Movimiento (origen destino, ej: e2 e4), o 'salir': ");

            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine().trim();

            if (line.equalsIgnoreCase("salir")) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\s+");
            if (parts.length != 2) {
                System.out.println("Formato inválido. Ejemplo: e2 e4");
                continue;
            }

            try {
                Position from = PositionParser.parse(parts[0]);
                Position to = PositionParser.parse(parts[1]);
                MoveResult result = game.move(from, to);

                if (!result.isSuccess()) {
                    System.out.println("Movimiento inválido: " + result.getFailureReason());
                    continue;
                }

                renderer.render(game);
                if (result.isCheck()) {
                    System.out.println("¡Jaque!");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        scanner.close();
        System.out.println("Fin de la partida.");
    }
}
