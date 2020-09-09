package controllers;

import com.google.gson.Gson;

import io.javalin.Javalin;

import java.io.IOException;
import java.util.Arrays;
import java.util.Queue;

import models.GameBoard;
import models.Player;

import org.eclipse.jetty.websocket.api.Session;

class PlayGame {

  private static final int PORT_NUMBER = 8080;

  private static Javalin app;

  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {

    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(PORT_NUMBER);

    // Test Echo Server
    app.post("/echo", ctx -> {
      ctx.result(ctx.body());
    });
    
    // Create a new game
    app.get("/newgame", ctx -> {
      ctx.redirect("/tictactoe.html");
    });
    
    // Start a new game
    app.post("/startgame", ctx -> {
      final char type = ctx.formParam("type").charAt(0);
      
      final GameBoard board = new GameBoard(new Player(type, 1), null,
          false, 1, new char[3][3], 0, false);
      
      ctx.result(new Gson().toJson(board)).contentType("application/json");
    });
    
    // Join an existing game
    app.get("/joingame", ctx -> {
      ctx.redirect("/tictactoe.html?p=2");
    });

    /**
     * Please add your end points here.
     * 
     * 
     * 
     * 
     * Please add your end points here.
     * 
     * 
     * 
     * 
     * Please add your end points here.
     * 
     * 
     * 
     * 
     * Please add your end points here.
     * 
     */

    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }

  /** Send message to all players.
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(gameBoardJson);
      } catch (IOException e) {
        // Add logger here
      }
    }
  }

  public static void stop() {
    app.stop();
  }
}
