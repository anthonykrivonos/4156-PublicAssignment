package controllers;

import models.GameBoard;
import models.Message;
import models.Player;

import com.google.gson.Gson;
import io.javalin.Javalin;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import org.eclipse.jetty.websocket.api.Session;

public class PlayGame {

  private static final int PORT_NUMBER = 8080;

  private static Javalin app;
  
  private static GameBoard board;

  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {

    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(PORT_NUMBER);
    
    // Create a new game
    app.get("/newgame", ctx -> {
      board = null;
      ctx.redirect("/tictactoe.html");
    });
    
    // Start a new game
    app.post("/startgame", ctx -> {
      if (ctx.formParam("type").isBlank() || (ctx.formParam("type").charAt(0) != 'X'
          && ctx.formParam("type").charAt(0) != 'O')) {
        ctx.status(500).result("Invalid type");
        return;
      }
    
      // Extract the player type and construct the board.
      final char type = ctx.formParam("type").charAt(0);
      board = new GameBoard(type);

      ctx.status(200).result(board.toJson());
    });
    
    // Join an existing game
    app.get("/joingame", ctx -> {
      if (board == null) {
        ctx.status(500).result("Board not initialized");
        return;
      }

      // Try to start the game
      try {
        board.joinGame();
      } catch (Exception e) {
        ctx.status(400).result(e.getMessage());
        return;
      }
      
      ctx.status(302).redirect("/tictactoe.html?p=2");
      // Send board after an async delay to allow p2 to redirect
      sendGameBoardToAllPlayers(board.toJson(), 1);
    });
    
    // Perform a move by the given player
    app.post("/move/:playerId", ctx -> {
      // Ensure the game has already started
      if (board == null || !board.isGameStarted()) {
        ctx.status(400).result("Game not started");
        return;
      }
      
      // Ensure the game is still going
      if (board.isGameOver()) {
        ctx.status(200).result(new Message(false, 101, "Game already over").toJson());
        return;
      }
      
      // Ensure a player ID is provided
      if (ctx.pathParam("playerId").length() != 1) {
        ctx.status(200).result(new Message(false, 102, "Invalid playerId").toJson());
        return;
      }
      
      // Ensure the player ID is valid
      final int playerId = Integer.parseInt(ctx.pathParam("playerId"));
      if (playerId != 1 && playerId != 2) {
        ctx.status(200).result(new Message(false, 103, "Incorrect playerId").toJson());
        return;
      }
      
      // Ensure position is provided
      if (ctx.formParam("x").isBlank() || ctx.formParam("y").isBlank()) {
        ctx.status(200).result(new Message(false, 104, "Missing position").toJson());
        return;
      }
      
      // Extract the necessary information to play a turn
      final Player player = playerId == 1 ? board.getP1() : board.getP2();
      final int x = Integer.parseInt(ctx.formParam("x"));
      final int y = Integer.parseInt(ctx.formParam("y"));
      
      // Try to play a turn and throw an exception if it cannot be played
      try {
        board.playTurn(player, x, y);
      } catch (Exception e) {
        ctx.status(200).result(new Message(false, 105, e.getMessage()).toJson());
        return;
      }
      
      ctx.status(200).result(new Message(true, 100, "").toJson());
      sendGameBoardToAllPlayers(board.toJson());
    });

    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }
  
  /**
   * Asynchronously calls sendGameBoardToAllPlayers after a delay.
   * Catches the IO exception and prints to stack trace.
   * @param gameBoardJson Gameboard JSON
   * @param secDelay The number of seconds to wait before the call.
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson, final int secDelay) {
    Thread thread = new Thread(new Runnable() {
      public void run() {
        try {
          TimeUnit.SECONDS.sleep(secDelay);
          sendGameBoardToAllPlayers(new Gson().toJson(board));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
    thread.start();
  }

  /**
   * Send message to all players.
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(gameBoardJson);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void stop() {
    app.stop();
  }
}
