package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import models.GameBoard;
import models.Player;

/**
 * A lightweight database that stores only one GameBoard object.
 */
public class Database {
  
  private static final String DATABASE_NAME = "tictactoe.db";
  private static final String GAMEBOARD_TABLE_NAME = "GB_TABLE";
  private static final String GAMEBOARD_ROW_ID = "0";
  
  private Connection connection;
  
  /**
   * Instantiate the database by creating a GameBoard table within it.
   * @throws SQLException An exception thrown when creating the connection or creating the table.
   */
  public Database() throws SQLException {
    // Create the database first
    connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
    
    // Create the table to store the gameboard
    Statement statement = connection.createStatement();
    String sql = "CREATE TABLE IF NOT EXISTS " + GAMEBOARD_TABLE_NAME + " (\n"
        + "ID INT PRIMARY KEY      NOT NULL, "
        + "P1_ID          INT      NOT NULL, "
        + "P1_TYPE        CHAR(1)  NOT NULL, "
        + "P2_ID          INT      NOT NULL, "
        + "P2_TYPE        CHAR(1)  NOT NULL, "
        + "GAME_STARTED   BOOLEAN  NOT NULL, "
        + "TURN           INT      NOT NULL, "
        + "WINNER         INT      NOT NULL, "
        + "BOARD_STATE    CHAR(9)  NOT NULL, "
        + "IS_DRAW        BOOLEAN  NOT NULL);";
    statement.executeUpdate(sql);
    
    // Clean up
    statement.close();
  }
  
  /**
   * Creates or updates the gameboard into the database.
   * @param gameboard The gameboard to set.
   * @throws SQLException An exception thrown when setting the GameBoard in the database.
   */
  public void set(GameBoard gameboard) throws SQLException {
    // Insert the gameboard into the database
    Statement statement = connection.createStatement();
    
    // Convert gameboard state into a string
    StringBuilder boardStateBuilder = new StringBuilder();
    for (char[] row : gameboard.getBoardState()) {
      for (char val : row) {
        boardStateBuilder.append(val == 0 ? '-' : val);
      }
    }
    String boardState = boardStateBuilder.toString();
    
    String sql = "REPLACE INTO " + GAMEBOARD_TABLE_NAME + " ("
        + "ID, P1_ID, P1_TYPE, P2_ID, P2_TYPE, GAME_STARTED,"
        + "TURN, WINNER, BOARD_STATE, IS_DRAW)\nVALUES ("
        + GAMEBOARD_ROW_ID + ", "
        + Integer.toString(gameboard.getP1().getId()) + ", "
        + "'" + gameboard.getP1().getType() + "', "
        + Integer.toString(gameboard.getP2() != null ? gameboard.getP2().getId() : 0) + ", "
        + "'" + (gameboard.getP2() != null ? gameboard.getP2().getType() : '-') + "', "
        + Integer.toString(gameboard.isGameStarted() ? 1 : 0) + ", "
        + Integer.toString(gameboard.getTurn()) + ", "
        + Integer.toString(gameboard.getWinner()) + ", "
        + "'" + boardState + "', "
        + Integer.toString(gameboard.isDraw() ? 1 : 0) + ");";
    statement.executeUpdate(sql);
    
    // Clean up
    statement.close();
  }
  
  /**
   * Get the gameboard from the database.
   * @return The gameboard or null.
   * @throws SQLException An exception thrown when the GameBoard is retrieved from the table.
   */
  public GameBoard get() throws SQLException {
    GameBoard gameboard;
    
    // Insert the gameboard into the database
    Statement statement = connection.createStatement();
    
    String sql = "SELECT * FROM " + GAMEBOARD_TABLE_NAME + " WHERE ID=" + GAMEBOARD_ROW_ID + ";";
    
    ResultSet rs = statement.executeQuery(sql);
    
    if (!rs.next()) {
      // Return null if no GameBoard is found
      return null;
    }
    
    // Get raw values from the row
    Player p1 = new Player(rs.getString("p1_type").charAt(0), rs.getInt("p1_id"));
    Player p2 = rs.getInt("p2_id") != 0
        ? new Player(rs.getString("p2_type").charAt(0), rs.getInt("p2_id"))
            : null;
    boolean gameStarted = rs.getBoolean("game_started");
    int turn = rs.getInt("turn");
    int winner = rs.getInt("winner");
    String boardStateString = rs.getString("board_state");
    boolean isDraw = rs.getBoolean("is_draw");
    
    // Rebuild board state
    char[][] boardState = new char[3][3];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        char val = boardStateString.charAt(i * 3 + j);
        boardState[i][j] = val == '-' ? 0 : val;
      }
    }
    
    // Instantiate the gameboard
    gameboard = new GameBoard(p1, p2, gameStarted, turn, boardState, winner, isDraw);
    
    // Clean up
    statement.close();
    
    return gameboard;
  }
  
  /**
   * Remove the GameBoard from the database.
   * @throws SQLException An exception thrown when the GameBoard is deleted from the table.
   */
  public void unset() throws SQLException {
    // Create the table to store the gameboard
    Statement statement = connection.createStatement();
    String sql = "DELETE FROM " + GAMEBOARD_TABLE_NAME + " WHERE ID=" + GAMEBOARD_ROW_ID + ";";
    statement.executeUpdate(sql);
    
    // Clean up
    statement.close();
  }
  
}
