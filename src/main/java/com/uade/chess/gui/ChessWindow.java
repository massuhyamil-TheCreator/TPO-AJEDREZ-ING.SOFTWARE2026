package com.uade.chess.gui;

import com.uade.chess.application.GameStatus;
import com.uade.chess.domain.PieceColor;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/** Adaptador JavaFX. Consume instantáneas y envía intenciones al controller. */
public final class ChessWindow {
    private final Stage stage;
    private final ChessController controller;
    private final ChessBoardView board;
    private final PieceRenderer pieces = new PieceRenderer();
    private final Label turn = label("", "turn-title");
    private final Label status = label("", "status-pill");
    private final Label feedback = label("", "feedback");
    private final Label selected = label("Seleccioná una pieza", "muted");
    private final Label moveCount = label("0 jugadas", "muted");
    private final Label lastMove = label("—", "last-move-text");
    private final ListView<HistoryRow> history = new ListView<>();
    private final PlayerBar blackPlayer = new PlayerBar(PieceColor.BLACK, "Jugador 2");
    private final PlayerBar whitePlayer = new PlayerBar(PieceColor.WHITE, "Jugador 1");
    private int renderedHistorySize = -1;

    public ChessWindow(Stage stage, ChessController controller) {
        this.stage = stage;
        this.controller = controller;
        board = new ChessBoardView(position -> {
            controller.click(position);
            render();
        });
    }

    public void show() {
        VBox root = new VBox(20);
        root.getStyleClass().add("app-root");
        root.getChildren().add(header());

        VBox boardColumn = new VBox(10);
        boardColumn.setMinWidth(440);
        boardColumn.getChildren().addAll(blackPlayer.root, board, whitePlayer.root, legend());
        VBox.setVgrow(board, Priority.ALWAYS);
        HBox.setHgrow(boardColumn, Priority.ALWAYS);

        VBox sidebar = sidebar();
        HBox main = new HBox(24, boardColumn, sidebar);
        VBox.setVgrow(main, Priority.ALWAYS);
        root.getChildren().addAll(main, footer());

        Scene scene = new Scene(root, 1120, 860);
        scene.getStylesheets().add(ChessWindow.class.getResource("/chess/styles/chess.css").toExternalForm());
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.cancelSelection();
                render();
            } else if (event.isControlDown() && event.getCode() == KeyCode.N) {
                requestNewMatch();
            } else if (event.isControlDown() && event.getCode() == KeyCode.F) {
                board.flip();
            } else return;
            event.consume();
        });
        stage.setTitle("Ajedrez · TPO Ingeniería de Software");
        stage.setMinWidth(990);
        stage.setMinHeight(780);
        stage.setScene(scene);
        render();
        stage.show();
    }

    private HBox header() {
        VBox branding = new VBox(3, label("AJEDREZ", "brand"),
                label("Una buena jugada empieza con una idea.", "muted"));
        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);
        Button flip = new Button("Girar tablero");
        flip.setId("flip-board");
        flip.getStyleClass().add("secondary-button");
        flip.setOnAction(event -> board.flip());
        Button help = new Button("Cómo jugar");
        help.setId("help");
        help.getStyleClass().add("secondary-button");
        help.setOnAction(event -> showHelp());
        HBox header = new HBox(12, branding, space, flip, help);
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private VBox sidebar() {
        VBox sidebar = new VBox(16);
        sidebar.setMinWidth(300);
        sidebar.setPrefWidth(300);
        sidebar.setMaxWidth(300);
        feedback.setWrapText(true);
        feedback.setMinHeight(60);
        feedback.setMaxWidth(Double.MAX_VALUE);
        feedback.setAlignment(Pos.CENTER_LEFT);
        feedback.setId("feedback");
        turn.setId("turn");
        status.setId("game-status");
        lastMove.setId("last-move");
        VBox gameCard = new VBox(12, label("PARTIDA LOCAL", "eyebrow"), turn, status,
                label("ÚLTIMA JUGADA", "eyebrow"), lastMove, feedback);
        gameCard.getStyleClass().add("card");

        HBox historyHeader = new HBox(8, label("Jugadas", "card-title"), spacer(), moveCount);
        historyHeader.setAlignment(Pos.CENTER_LEFT);
        HBox columns = new HBox(38, label("#", "eyebrow"), label("BLANCAS", "eyebrow"),
                label("NEGRAS", "eyebrow"));
        columns.getStyleClass().add("history-columns");
        history.setId("move-history");
        history.setMinHeight(110);
        VBox emptyHistory = new VBox(8, label("Tu primera jugada", "empty-title"),
                label("Seleccioná una pieza y su destino.\nEl historial aparecerá acá.", "empty-help"));
        emptyHistory.setAlignment(Pos.CENTER);
        history.setPlaceholder(emptyHistory);
        history.setCellFactory(view -> new ListCell<>() {
            @Override
            protected void updateItem(HistoryRow item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                if (empty || item == null) { setGraphic(null); return; }
                Label number = label(String.valueOf(item.number()), "history-number");
                number.setMinWidth(24);
                Label white = label(item.white(), "history-move");
                white.setMinWidth(101);
                Label black = label(item.black(), "history-move");
                black.setMinWidth(101);
                HBox row = new HBox(6, number, white, black);
                row.setAlignment(Pos.CENTER_LEFT);
                setGraphic(row);
            }
        });
        VBox historyCard = new VBox(12, historyHeader, columns, history);
        historyCard.getStyleClass().add("history-card");
        VBox.setVgrow(history, Priority.ALWAYS);
        VBox.setVgrow(historyCard, Priority.ALWAYS);

        Button newGame = new Button("Nueva partida");
        newGame.setId("new-match");
        newGame.getStyleClass().add("primary-button");
        newGame.setMaxWidth(Double.MAX_VALUE);
        newGame.setOnAction(event -> requestNewMatch());
        sidebar.getChildren().addAll(gameCard, historyCard, newGame);
        return sidebar;
    }

    private HBox legend() {
        Label dot = label("●", "legend-dot");
        Label hint = label("Destino legal", "muted");
        HBox row = new HBox(8, dot, hint, spacer(), selected);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("legend");
        return row;
    }

    private HBox footer() {
        HBox footer = new HBox(label("Ingeniería de Software · UADE", "footer-label"), spacer(),
                label("2 jugadores · Sin reloj", "footer-label"));
        footer.getStyleClass().add("footer");
        return footer;
    }

    private void render() {
        ChessViewState state = controller.state();
        board.render(state);
        turn.setText("Turno de " + PresentationText.color(state.turn()).toLowerCase());
        status.setText(switch (state.status()) {
            case CHECK -> "JAQUE · Protegé a tu rey";
            case CHECKMATE -> "JAQUE MATE · Ganan las " + PresentationText.color(state.turn().opposite());
            case STALEMATE -> "AHOGADO · Tablas, sin movimientos legales";
            case IN_PROGRESS -> "En progreso";
        });
        boolean alert = state.status() == GameStatus.CHECK || state.status() == GameStatus.CHECKMATE
                || state.status() == GameStatus.STALEMATE;
        status.getStyleClass().setAll("status-pill", alert ? "status-check" : "status-normal");
        feedback.setText(state.message());
        feedback.getStyleClass().setAll("feedback", state.error() ? "feedback-error" : "feedback-info");
        selected.setText(state.selected() == null ? "Seleccioná una pieza"
                : state.selected() + " · " + state.legalMoves().size() + " destinos");
        lastMove.setText(state.lastMove() == null ? "—" : state.lastMove().notation());
        blackPlayer.render(state);
        whitePlayer.render(state);
        if (renderedHistorySize != state.history().size()) {
            List<HistoryRow> rows = new ArrayList<>();
            for (int index = 0; index < state.history().size(); index += 2) {
                String white = state.history().get(index).notation();
                String black = index + 1 < state.history().size() ? state.history().get(index + 1).notation() : "—";
                rows.add(new HistoryRow(index / 2 + 1, white, black));
            }
            history.getItems().setAll(rows);
            moveCount.setText(state.history().size() + (state.history().size() == 1 ? " jugada" : " jugadas"));
            if (!rows.isEmpty()) Platform.runLater(() -> history.scrollTo(history.getItems().size() - 1));
            renderedHistorySize = state.history().size();
        }
    }

    private void requestNewMatch() {
        if (!controller.state().history().isEmpty()) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Se reiniciarán el tablero, las capturas y el historial.", ButtonType.CANCEL, ButtonType.OK);
            prepareAlert(confirm, "Nueva partida", "¿Empezar una nueva partida?");
            if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;
        }
        controller.newMatch();
        render();
    }

    private void showHelp() {
        Alert help = new Alert(Alert.AlertType.INFORMATION);
        prepareAlert(help, "Cómo jugar", "Dos jugadores, una misma pantalla");
        help.setContentText("1. Elegí una pieza del jugador que tiene el turno.\n"
                + "2. Elegí su destino: los puntos y anillos indican jugadas legales.\n"
                + "3. Para cambiar de pieza, tocá otra del mismo color.\n\n"
                + "Esc: cancelar selección · Ctrl+F: girar · Ctrl+N: nueva partida.\n"
                + "También podés recorrer los botones con Tab y activarlos con Espacio.\n\n"
                + "Esta versión incluye las reglas del backend entregado: movimientos,\n"
                + "turnos, capturas, jaque y prevención del autojaque.\n"
                + "Enroque, promoción, captura al paso, mate y tablas no están implementados.");
        help.getDialogPane().setPrefWidth(580);
        help.showAndWait();
    }

    private void prepareAlert(Alert alert, String title, String header) {
        alert.initOwner(stage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.getDialogPane().getStylesheets().add(
                ChessWindow.class.getResource("/chess/styles/chess.css").toExternalForm());
    }

    private static Label label(String text, String style) {
        Label label = new Label(text);
        label.getStyleClass().add(style);
        return label;
    }

    private static Region spacer() {
        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);
        return space;
    }

    private record HistoryRow(int number, String white, String black) { }

    private final class PlayerBar {
        private final PieceColor player;
        private final HBox root;
        private final Label activity = label("", "player-subtitle");
        private final FlowPane captures = new FlowPane(1, 1);
        private final Label noCaptures = label("Sin capturas", "player-subtitle");

        private PlayerBar(PieceColor player, String name) {
            this.player = player;
            Label badge = label(player == PieceColor.WHITE ? "B" : "N", "player-badge");
            badge.getStyleClass().add(player == PieceColor.WHITE ? "white-badge" : "black-badge");
            VBox identity = new VBox(3, label(PresentationText.color(player), "player-name"), activity);
            identity.setMinWidth(Region.USE_PREF_SIZE);
            captures.setAlignment(Pos.CENTER_RIGHT);
            captures.setMinWidth(0);
            captures.setPrefWidth(0);
            captures.setMaxWidth(400);
            noCaptures.setMinWidth(Region.USE_PREF_SIZE);
            HBox.setHgrow(captures, Priority.ALWAYS);
            root = new HBox(12, badge, identity, captures, noCaptures);
            root.setAlignment(Pos.CENTER_LEFT);
            root.getStyleClass().add("player-bar");
            root.setId(player == PieceColor.WHITE ? "white-player" : "black-player");
            root.setUserData(name);
        }

        private void render(ChessViewState state) {
            boolean active = state.turn() == player;
            root.getStyleClass().setAll("player-bar", active ? "active-player" : "waiting-player");
            activity.setText(active ? "Tu turno" : (String) root.getUserData());
            captures.getChildren().clear();
            for (var captured : state.capturedBy(player)) {
                Region icon = pieces.create(captured);
                icon.setPrefSize(24, 30);
                icon.setMinSize(24, 30);
                icon.setMaxSize(24, 30);
                captures.getChildren().add(icon);
            }
            noCaptures.setVisible(captures.getChildren().isEmpty());
            noCaptures.setManaged(captures.getChildren().isEmpty());
        }
    }
}
